//GET ALL OFFICES
function loadTable() {
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/offices");
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var trHTML = '';
            const objects = JSON.parse(this.responseText);
            for (let object of objects) {

                trHTML += '<td>' + object['id'] + '</td>';
                trHTML += '<td>' + object['location'] + '</td>';
                trHTML += '<td><button type="button" class="btn btn-outline-secondary" onclick="showOfficeEditBox(' + object['id'] + ')">Edit</button>';
                trHTML += '<button type="button" class="btn btn-outline-danger" onclick="showDeleteWarning(' + object['id'] + ')">Del&nbsp</button></td>';
                trHTML += "</tr>";
            }
            document.getElementById("mytable").innerHTML = trHTML;
        }
    };
}
loadTable();


function validateFormFields(){
  var elements = document.getElementsByClassName("swal2-input");

   let valid=true;
   for (var i = 0, len = elements.length; i < len; i++){
       if((elements[i].value=="" || elements[i].value==null) && elements[i].style.display!="none" ){
       valid=false;
      Swal.showValidationMessage(elements[i].placeholder+ ' should not be empty!');
     break;
       }

    }
    return valid;
    }

//CREATE NEW OFFICE
function showOfficeCreateBox() {
    Swal.fire({
        title: 'Add New Office',
        html:
            '<input id="id" type="hidden">' +
            '<input id="location" class="swal2-input" placeholder="Office Address">',
        focusConfirm: false,
        backdrop: `
            rgba(0,0,123,0.4)
            url("/images/nyan-cat.gif")
            left top
            no-repeat
          `,
        preConfirm: () => {
           if(validateFormFields()){
           officeCreate();
           }
        }
    })
}

function officeCreate() {

    const location = document.getElementById("location").value;


    const xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://localhost:8080/office");
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "location": location
    }));

    xhttp.onreadystatechange = function () {
        console.log(this.status);
        if (this.readyState == 4 && this.status == 201) {
            const objects = JSON.parse(this.responseText);
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500,
                text: 'new branch opened in '+location
            });
            loadTable();
        }
        else {
            const objects =JSON.parse(this.responseText);
            Swal.fire({
                icon: 'error',
                title: objects['message']
            })
        }
    };
}


//UPDATE OFFICE ADDRESS
function showOfficeEditBox(id) {
    console.log(id);
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/office/" + id);
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            const objects = JSON.parse(this.responseText);
            console.log(objects);
            const office = objects;
            console.log(office);
            Swal.fire({
                title: 'Edit Office',
                html:
                    '<input id="id" type="hidden" value=' + office['id'] + '>' +
                    '<input id="location" class="swal2-input" placeholder="office Location" value="' + office['location'] + '">',
                focusConfirm: false,
                preConfirm: () => {
                    if(validateFormFields()){
                    officeEdit();
                    }
                }
            })
        }
    };
}

function officeEdit() {
    const id = document.getElementById("id").value;
    const location = document.getElementById("location").value;

    const xhttp = new XMLHttpRequest();
    xhttp.open("PUT", "http://localhost:8080/office/" + id);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "location": location
    }));
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            const objects = JSON.parse(this.responseText);
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500,
                text: 'Location updated'
            });
            loadTable();
        }
        else {
            const objects =JSON.parse(this.responseText);
            Swal.fire({
                icon: 'error',
                title: objects['message']
            })
        }
    };
}


//DELETE AN OFFICE
function showDeleteWarning(id) {
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: 'btn btn-success',
            cancelButton: 'btn btn-danger'
        },
        buttonsStyling: false
    })

    swalWithBootstrapButtons.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, cancel!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            officeDelete(id);

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelled',
                '',
                'error'
            )
        }
    })
}

function officeDelete(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.open("DELETE", "http://localhost:8080/office/" + id);
    xhttp.send();
    xhttp.onreadystatechange = function () {

        if (this.readyState == 4 && this.status == 200 ) {
         const objects =this.responseText;
            Swal.fire(
            'Deleted!',
            objects,
            'success'
            )
        }
        else {
         const objects =JSON.parse(this.responseText);
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: objects['message'],
                footer: '<a href="">Why do I have this issue?</a>'
            })
        }
        loadTable();
    };
}

