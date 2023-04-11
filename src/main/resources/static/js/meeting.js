
const loginButton = document.getElementById("login-btn");
const loginForm = document.getElementById("login-form");

var login_name = null;
var login_id = null;
var login_email = null;


function error() {
    Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Something went wrong!',
        confirmButtonText: "retry"
    })
}

if (typeof (loginButton) != 'undefined' && loginButton != null) {
    loginButton.addEventListener("click", (e) => {
        e.preventDefault();
        var username = loginForm.username.value;
        const xhttp = new XMLHttpRequest();
        console.log(Number.isFinite(username))
        if (Number.isFinite(username)) {
            console.log("Received EmployeeId");
            xhttp.open("GET", "http://localhost:8080/employee?id=" + username);
        }
        else {
            console.log("Received Employee Email ");
            xhttp.open("GET", "http://localhost:8080/employee?email=" + username);
        }

        xhttp.send();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                const object = JSON.parse(this.responseText);
                localStorage.setItem("session-id", object['id']);
                localStorage.setItem("session-email", object['email']);
                localStorage.setItem("session-name", object['name']);
                localStorage.setItem("session-off", object['office']['id']);

                let timerInterval;
                Swal.fire({
                    title: 'Logging in',
                    timer: 200,
                    timerProgressBar: true,
                    didOpen: () => {
                        Swal.showLoading()
                        timerInterval = setInterval("", 10)
                    },
                    willClose: () => {
                        clearInterval(timerInterval)
                        window.open('./meeting.html', '_self');
                    }
                }).then((result) => {
                    if (result.dismiss === Swal.DismissReason.timer) {
                        console.log('Granted Access')
                    }
                })
            }
            else
                error();
        }
    });
}
else {

    login_name = localStorage.getItem("session-name");
    login_id = localStorage.getItem("session-id");
    login_email = localStorage.getItem("session-email");
    console.log(login_name);
    document.getElementById('nav-user-id').innerHTML = '<b style="color:#ffffff;padding-top:10px;letter-spacing: 1px;" >Welcome ' + login_name + '</b>';
    loadTable('');
}


//GET ALL MEETING WITH FILTERS
function loadTable(filter) {
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/meeting?id=" + login_id + filter);
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var trHTML = '';
            const objects = JSON.parse(this.responseText);
            let color = 'black';
            let hidden = '';
            let status = 'Accept';
            let cstatus = 'PENDING';
            for (let object of objects) {
                color = 'black';
                hidden = '';

                if (object['status'] === 'CANCELLED') {
                    color = 'red';
                    hidden = 'hidden';
                    break;  // hide cancelled  meeting
                }
                trHTML += '<tr style="color:' + color + '">';
                trHTML += '<td>' + object['id'] + '</td>';
                trHTML += '<td>' + object['agenda'] + '</td>';
                trHTML += '<td>' + object['description'] + '</td>';
                trHTML += '<td>' + object['allocatedRoomId'] + '</td>';
                trHTML += '<td>' + object['host'] + '</td>';
                trHTML += '<td>' + object['start'] + '</td>';
                trHTML += '<td>' + object['end'] + '</td>';
                trHTML += '<td>';
                for (let attendee of object['attendees']) {
                    trHTML += attendee + "<br>";
                }
                const xhttp2 = new XMLHttpRequest();
                xhttp2.open("GET", "http://localhost:8080/attendee-status?email=" + login_email + '&meetingId=' + object['id'], false);
                xhttp2.send(null);
                const statusObject = xhttp2.responseText;
                if (xhttp2.readyState == 4 && xhttp2.status == 200) {
                    if (statusObject === 'YES') {
                        status = 'Decline';
                        cstatus = 'NO';
                    }
                    else if (statusObject === 'NO') {
                        status = 'Accept';
                        cstatus = 'YES';

                    }
                    else if (statusObject === 'PENDING') {
                        status = 'Accept';
                        cstatus = 'YES';
                    }
                }
                else {
                    status = 'Accept';
                }


                trHTML += '</td>';
                trHTML += '<td><button type="button" class="btn btn-outline-secondary" onclick="changeStatus(' + object['id'] + ',' + '\'' + cstatus + '\'' + ')"' + hidden + '>' + status + '</button>';

                if(login_email.localeCompare(object['host'])==0){
                trHTML += '<button type="button" class="btn btn-outline-danger" onclick="showCancelMeetingBtn(' + object['id'] + ',' + '\'' + object['host'] + '\'' + ')"' + hidden + '>Cancel&nbsp</button></td>';
                }

                trHTML += "</tr>";

            }
            document.getElementById("mytable").innerHTML = trHTML;
        }
    };
}

function validateFormFields(){

   var elements = document.getElementsByClassName("swal2-input");

     let valid=true;
     for (var i = 0, len = elements.length; i < len; i++) {
         if((elements[i].value=="" || elements[i].value==null) && elements[i].style.display!="none" ){
         valid=false;
        Swal.showValidationMessage(elements[i].placeholder+ ' should not be empty!');
       break;
         }
         if(elements[i].id=="allocatedRoomId"){
          if (isNaN(elements[i].value))
           {
           valid=false;
            Swal.showValidationMessage(elements[i].placeholder+ ' should be number!');
           }
         }
     }
return valid;

}

let count;
//CREATE NEW MEETING
function showMeetingCreateBox() {
    count = 2;
    Swal.fire({
        title: 'Create a New Meeting',
        html:
            '<input id="id" type="hidden">' +
            '<input id="agenda" class="swal2-input" placeholder="Meeting Agenda">' +
            '<input id="description" class="swal2-input" placeholder="Description">' +
            '<input type="datetime-local" id="start" class="swal2-input" style="padding: 0 4.0em" placeholder="Start Time">' +
            '<input type="datetime-local" id="end" class="swal2-input" style="padding: 0 4.0em" placeholder="End Time">' +
            '<input id="allocatedRoomId" class="swal2-input" placeholder="Allocated Room Id">' +
            '<div id="attendee-div"></div>' +
            '<input id="attendee1" required type="email" class="swal2-input" placeholder="Attendee Email">' +
            '<button type="button" class="btn btn-outline-secondary" onclick="addTextBox()" >+</button>' +
            '',
        focusConfirm: false,
        preConfirm: () => {

        if(validateFormFields()){
            meetingCreate();
            }

   }
    })
}

function showMeetingFilterBox() {
    Swal.fire({
        title: 'Filter Meetings',
        html:
            `<select style="padding:0 5em;" name="op" id="filter" class="swal2-input" placeholder="Operational">
                <option value="" disabled selected>Filters</option>
                <option value="current_week">Current Week</option>
                <option value="next_week">Next Week</option>
                <option value="last_week">Last Week</option>
            </select>` +
            '<input type="date" id="start" class="swal2-input" style="padding: 0 4.0em" placeholder="Start Time">' +
            '<input type="date" id="end" class="swal2-input" style="padding: 0 4.0em" placeholder="End Time">',
        focusConfirm: false,
        preConfirm: () => {
            const filterVal = document.getElementById("filter").value;
            const start = document.getElementById("start").value.replaceAll('-', '/');
            const end = document.getElementById("end").value.replaceAll('-', '/');
            if (start !== '' && end !== '') {
                loadTable('&start=' + start + '&end=' + end);
            }
            else if (filterVal != '') {
                loadTable("&filter=" + filterVal);
            }
        }
    })
}

function addTextBox() {
    var divObj = document.getElementById("attendee-div");
    var node = document.createElement("INPUT");
    node.setAttribute("type", "email");
    node.setAttribute("class", "swal2-input");
    node.setAttribute("placeholder", "Attendee Email");
    node.setAttribute("id", "attendee" + count);
    if (count < 6) {
        divObj.append(node);
        count++;
    }else{
   Swal.showValidationMessage('Attendees should be less than 6')
    }

}


function meetingCreate() {
    const agenda = document.getElementById("agenda").value;
    const description = document.getElementById("description").value;
    const start = document.getElementById("start").value.replace('T', ' ');
    const end = document.getElementById("end").value.replace('T', ' ');

    var allocatedRoomId = document.getElementById("allocatedRoomId");
    if (typeof (allocatedRoomId) != 'undefined' && allocatedRoomId != null)
        allocatedRoomId = allocatedRoomId.value;
    else allocatedRoomId = null;
    const attendeeList = [];
    let temp = 0;
    count--;
    while (temp++ < count) {
        var a = "attendee" + temp;
        console.log(a);
        attendeeList.push(document.getElementById(a).value);
    }

    const xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://localhost:8080/meeting",true);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(
    JSON.stringify({
        "agenda": agenda,
        "description": description,
        "host": login_email,
        "start": start,
        "end": end,
        "allocatedRoomId": allocatedRoomId,
        "attendees": attendeeList

    })
    );

    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            const objects = this.responseText;
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500,
                text: 'Meeting with id:' + objects + ' has been created '
            });

            loadTable('');
        }
        else if (this.readyState == 4  && this.status == 400) {
            let objects =JSON.parse(this.responseText);
            console.log(this)
            Swal.fire({
                icon: 'error',
                title: objects['message'],
            })
        }
        else {
            const objects = JSON.parse(this.responseText);
            console.log(this);
            console.log(objects['timestamp']);
            console.log(objects['message']);

            Swal.fire({
                icon: 'error',
                title: objects['message'],

            })
        }
    };
}
function showCancelMeetingBtn(id, hostEmail) {
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
        confirmButtonText: 'Yes, Cancel it!',
        cancelButtonText: 'No, Don\'t cancel!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            cancelMeeting(id, hostEmail);

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Aborted',
                '',
                'error'
            )
        }
    })
}

function cancelMeeting(id, hostEmail) {
    const xhttp = new XMLHttpRequest();
    if (!(hostEmail == localStorage.getItem("session-email"))) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'You are not the host of the meeting!'
        });
        return false;
    }
    xhttp.open("PUT", "http://localhost:8080/cancel-meeting/" + id);
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            const objects = this.responseText;
            Swal.fire(
                'Cancelled!',
                'Meeting has been cancelled.',
                'success'
            )
        }
        else {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
                footer: '<a href="">Why do I have this issue?</a>'
            });
        }
        loadTable('');
    };

}
function changeStatus(meetingId, status) {
    console.log("value of status : " + status);
    const xhttp = new XMLHttpRequest();
    xhttp.open("PUT", "http://localhost:8080/attendee-status")
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "meetingId": meetingId,
        "employeeId": login_id,
        "isAttending": status
    }));
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            //const objects = this.responseText;
            Swal.fire({
                icon: 'success',
                title: 'Status changed to ' + status,
                showConfirmButton: false,
                timer: 1500
            })
            loadTable('');
        }
    }
}
