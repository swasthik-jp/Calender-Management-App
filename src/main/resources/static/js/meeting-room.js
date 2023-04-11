//GET ALL OFFICES
function loadTable() {
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/meetingrooms");
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var trHTML = '';
            const objects = JSON.parse(this.responseText);
            for (let object of objects) {
                trHTML += '<tr>';
                trHTML += '<td>' + object['id'] + '</td>';
                trHTML += '<td>' + object['name'] + '</td>';
                trHTML += '<td>' + object['office']['id'] + '</td>';
                trHTML += '<td>' + object['capacity'] + '</td>';
                trHTML += '<td>' + object['operational'] + '</td>';

                trHTML += '<td><button type="button" class="btn btn-outline-secondary" onclick="showMeetingRoomEditBox(' + object['id'] + ')">Edit</button>';
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
         for (var i = 0, len = elements.length; i < len; i++) {
             if((elements[i].value=="" || elements[i].value==null) && elements[i].style.display!="none" ){
             valid=false;
            Swal.showValidationMessage(elements[i].placeholder+ ' should not be empty!');
           break;
              }
              if(elements[i].id=="office-id" || elements[i].id=="capacity"){
               if (isNaN(elements[i].value))
                {
                valid=false;
                 Swal.showValidationMessage(elements[i].placeholder+ ' should be number!');
                       break;
           }
             }
          }

           return valid;
}

//CREATE NEW MEETING ROOM
function showMeetingRoomCreateBox() {
    Swal.fire({
        title: 'Add New Meeting Room',

        html:
            '<input id="id" type="hidden">' +
            '<input id="room-name" class="swal2-input" placeholder="Room Name">' +
            '<input id="office-id" class="swal2-input" placeholder="Office ID">' +
            '<input id="capacity" class="swal2-input" placeholder="Room Capacity">'+
            `<select style="padding:0 5em;" name="op" id="is-op" class="swal2-input" placeholder="Operational">
                <option id="operational" value="" disabled selected>Operational</option>
                <option value="true">True</option>
                <option value="false">False</option>
              </select>`,

        focusConfirm: false,
        preConfirm: () => {

       if(validateFormFields()){
        meetingRoomCreate();
         }
        }
    })
}

function meetingRoomCreate() {
    const name = document.getElementById("room-name").value;
    const officeId = document.getElementById("office-id").value;
    const capacity = document.getElementById("capacity").value;
    const isOperational = document.getElementById("is-op").value;

    const xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://localhost:8080/meetingroom");
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "name": name,
        "office": {
            "id": officeId
        },
        "capacity": capacity,
        "isOperational": isOperational
    }));
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            const objects = JSON.parse(this.responseText);
            //console.log("Inside Error Handler : " + objects);
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500,
                text: 'New Meeting Room created'
            });
            loadTable();
        }
        else {
        const objects = JSON.parse(this.responseText);
            Swal.fire({
                icon: 'error',
                title: objects['message']
            })
        }
    };
}


//UPDATE OMEETING ROOM
function showMeetingRoomEditBox(id) {

    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/meetingroom/" + id);
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            const objects = JSON.parse(this.responseText);
            const meetingRoom = objects;
            console.log(meetingRoom);
            var truePlaceHolder = `<select style="padding:0 5em;" name="op" id="is-op" class="swal2-input" placeholder="Operational">
                <option value="" disabled selected>Operational</option>
                <option value="true" `;
            var falsePlaceHolder = '<option value="false"'
            if (meetingRoom['operational'] === true) {
                truePlaceHolder += 'selected';
            }
            else {
                falsePlaceHolder += 'selected';
            }
            truePlaceHolder += '/>True</option>';
            falsePlaceHolder += '/>False</option></select>';
            Swal.fire({
                title: 'Edit MeetingRoom',
                html:
                    '<input id="id" type="hidden" value=' + meetingRoom['id'] + '>' +
                    '<input id="room-name" class="swal2-input" placeholder="Meeting room name" value="' + meetingRoom['name'] + '">' +
                    '<input id="office-id" class="swal2-input" placeholder="Office-Id" value="' + meetingRoom['office']['id'] + '">' +
                    '<input id="capacity" class="swal2-input" placeholder="Capacity" value="' + meetingRoom['capacity'] + '">' +
                    truePlaceHolder +
                    falsePlaceHolder,

                focusConfirm: false,
                preConfirm: () => {



              if(validateFormFields()){
                      meetingRoomEdit();
                  }

                }
            })
        }else {
                 const objects = JSON.parse(this.responseText);
                     Swal.fire({
                         icon: 'error',
                         title: objects['message']
                     })
                 }
    };
}

function meetingRoomEdit() {
    const id = document.getElementById("id").value;
    const name = document.getElementById("room-name").value;
    const officeId = document.getElementById("office-id").value;
    const capacity = document.getElementById("capacity").value;
    const isOperational = document.getElementById("is-op").value;

    const xhttp = new XMLHttpRequest();
    xhttp.open("PUT", "http://localhost:8080/meetingroom/" + id);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "name": name,
        "office": {
            "id": officeId
        },
        "capacity": capacity,
        "isOperational": isOperational
    }));
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            const objects = JSON.parse(this.responseText);
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500,
                text: 'Renovation Completed'
            });
            loadTable();
        }
        else {
            const objects = JSON.parse(this.responseText);
            Swal.fire({
                icon: 'error',
                title: objects['message']
            })
        }
    };
}

//DELETE MEETING ROOM
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
            meetingRoomDelete(id);

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

function meetingRoomDelete(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.open("DELETE", "http://localhost:8080/meetingroom/" + id);
    xhttp.send();
    xhttp.onreadystatechange = function () {

        if (this.readyState == 4 && this.status == 200 ) {
          const objects = this.responseText;
            Swal.fire(
            'Deleted!',
            objects,
            'success'
            )
        }
        else {
          const objects = JSON.parse(this.responseText);
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
