//GET ALL EMPLOYEE
function loadTable() {
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/employees");
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var trHTML = '';
            const objects = JSON.parse(this.responseText);
            for (let object of objects) {
                trHTML += '<tr>';
                trHTML += '<td>' + object['id'] + '</td>';
                trHTML += '<td>' + object['name'] + '</td>';
                trHTML += '<td>' + object['email'] + '</td>';
                trHTML += '<td>' + object['dob'] + '</td>';
                trHTML += '<td>' + object['address'] + '</td>';
                trHTML += '<td>' + object['ph'] + '</td>';
                trHTML += '<td>' + object['office']['location'] + '</td>';
                trHTML += '<td><button type="button" class="btn btn-outline-secondary" onclick="showEmployeeEditBox(' + object['id'] + ')">Edit</button>';
                trHTML += '<button type="button" class="btn btn-outline-danger" onclick="showDeleteWarning(' + object['id'] + ')">Del</button></td>';
                trHTML += "</tr>";
            }
            document.getElementById("mytable").innerHTML = trHTML;
        }
    };
}
loadTable();

//CREATE NEW EMPLOYEE
function showEmployeeCreateBox() {
    Swal.fire({
        title: 'Add New Employee',
        html:
            '<input id="id" type="hidden">' +
            '<input id="name" class="swal2-input" placeholder="Full Name">' +
            '<input id="email" class="swal2-input" placeholder="Email Address">' +
            '<input type="date"  id="dob" class="swal2-input" style="padding: 0 4.0em" placeholder="Date Of Birth">' +
            '<input id="address" class="swal2-input" placeholder="Home Address">' +
            '<input id="ph" class="swal2-input" placeholder="Phone No">' +
            '<input id="offId" class="swal2-input" placeholder="Office ID">',
        focusConfirm: false,
        preConfirm: () => {
            employeeCreate();
        }
    })
}

function employeeCreate() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const dob = document.getElementById("dob").value;
    const address = document.getElementById("address").value;
    const ph = document.getElementById("ph").value;
    const offId = document.getElementById("offId").value;

    const xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://localhost:8080/employee");
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "name": name,
        "email": email,
        "dob": dob,
        "office": {
            "id": offId,
        },
        "address": address,
        "ph": ph

    }));
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            const objects = JSON.parse(this.responseText);
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500,
                text: objects['name'] + ' has joined '
            });
            loadTable();
        }
        else if(this.status == 400){
            const objects = this.responseText;
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: objects['errors'] ,
                footer: '<a href="">Why do I have this issue?</a>'
            })
        }
        else {
            const objects = this.responseText;
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!'+ objects['message'] ,
                footer: '<a href="">Why do I have this issue?</a>'
            })
        }
    };
}


//UPDATE EMPLOYEE
function showEmployeeEditBox(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/employee/" + id);
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            const employee = JSON.parse(this.responseText);
            console.log(employee);
            Swal.fire({
                title: 'Edit Employee',
                html:
                    '<input id="id" type="hidden" value=' + employee['id'] + '>' +
                    '<input id="name" class="swal2-input" value=' + employee['name'] + '>' +
                    '<input id="email" class="swal2-input" placeholder="Email Address" value =' + employee['email'] + '>' +
                    '<input type="date" id="dob" class="swal2-input" style="padding: 0 4.0em" placeholder="Date Of Birth" value =' + employee['dob'] + '>' +
                    '<input id="address" class="swal2-input" placeholder="Home Address" value =' + employee['address'] + '>' +
                    '<input id="ph" class="swal2-input" placeholder="Phone No" value =' + employee['ph'] + '>' +
                    '<input id="offId" class="swal2-input" placeholder="Office ID" value =' + employee['office']['id'] + '>',
                focusConfirm: false,
                preConfirm: () => {
                    employeeEdit();
                }
            })
        }
    };
}

function employeeEdit() {
    const id = document.getElementById("id").value;
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const dob = document.getElementById("dob").value;
    const address = document.getElementById("address").value;
    const ph = document.getElementById("ph").value;
    const offId = document.getElementById("offId").value;

    const xhttp = new XMLHttpRequest();
    xhttp.open("PUT", "http://localhost:8080/employee/" + id);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "name": name,
        "email": email,
        "dob": dob,
        "office": {
            "id": offId,
        },
        "address": address,
        "ph": ph
    }));
    xhttp.onreadystatechange = function () {
        console.log(this.responseStatus)
        if (this.readyState == 4 && this.status == 200) {
            //const objects = this.responseText;
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500
            })
            loadTable();
        }
        if (this.status == 400 || this.status == 404 ) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
                footer: '<a href="">Why do I have this issue?</a>'
            })
        }
    };
}




//DELETE AN EMPLOYEE
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
            employeeDelete(id);

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

function employeeDelete(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.open("DELETE", "http://localhost:8080/employee?id=" + id);
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200 ) {
            const objects = this.responseText;
            Swal.fire(
            'Deleted!',
            'Employee has been deleted.',
            'success'
            )
        }
        else {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
                footer: '<a href="">Why do I have this issue?</a>'
            })
        }
        loadTable();
    };
}