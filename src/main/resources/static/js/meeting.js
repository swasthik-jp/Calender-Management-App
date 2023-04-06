
const loginButton = document.getElementById("login-btn");
const loginForm = document.getElementById("login-form");

var login_name= null;
var login_id = null;
var login_email = null;


function error(){
Swal.fire({
  icon: 'error',
  title: 'Oops...',
  text: 'Something went wrong!',
  confirmButtonText: "retry"
})}

if (typeof(loginButton) != 'undefined' && loginButton != null)
{
    loginButton.addEventListener("click", (e) => {
        e.preventDefault();
        var username = loginForm.username.value;
        const xhttp = new XMLHttpRequest();
        console.log(Number.isFinite(username))
        if (Number.isFinite(username)) {
            console.log("Received EmployeeId");
            xhttp.open("GET", "http://localhost:8080/employee?id=" + username);
        }
        else{
            console.log("Received Employee Email ");
            xhttp.open("GET", "http://localhost:8080/employee?email=" + username);
        }

        xhttp.send();
        xhttp.onreadystatechange = function () {
            if (this.readyState==4 && this.status == 200) {
                const object = JSON.parse(this.responseText);
                localStorage.setItem("session-id", object['id']);
                localStorage.setItem("session-email",object['email']);
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
                    window.open('./meeting.html','_self');
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
    })
}
else{

    login_name = localStorage.getItem("session-name");
    login_id = localStorage.getItem("session-id");
    login_email = localStorage.getItem("session-email");
    console.log(login_name);
    document.getElementById('nav-user-id').innerHTML = '<b style="color:#ffffff;padding-top:10px;letter-spacing: 1px;" >Welcome '+ login_name + '</b>';

    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8080/meeting?id="+ login_id);
    loadTable(xhttp);
}


//GET ALL MEETING WITH FILTERS
function loadTable(xhttp) {
    xhttp.send();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var trHTML = '';
            const objects = JSON.parse(this.responseText);
            for (let object of objects) {
                trHTML += '<tr>';
                trHTML += '<td>' + object['id'] + '</td>';
                trHTML += '<td>' + object['agenda'] + '</td>';
                trHTML += '<td>' + object['description'] + '</td>';
                trHTML += '<td>' + object['allocatedRoomId'] + '</td>';
                trHTML += '<td>' + object['host'] + '</td>';
                trHTML += '<td>' + object['start'] + '</td>';
                trHTML += '<td>' + object['end'] + '</td>';
                trHTML += '<td>' ;
                for(let attendee of object['attendees']){
                    trHTML += attendee + "<br>"
                }

                trHTML += '</td>';
                trHTML += '<td><button type="button" class="btn btn-outline-secondary" onclick="showEmployeeEditBox(' + object['id'] + ')">Edit</button>';
                trHTML += '<button type="button" class="btn btn-outline-danger" onclick="showDeleteWarning(' + object['id'] + ')">Del&nbsp</button></td>';
                trHTML += "</tr>";
            }
            document.getElementById("mytable").innerHTML = trHTML;
        }
    };
}

let count;
//CREATE NEW MEETING
function showMeetingCreateBox() {
    count =2;
    Swal.fire({
        title: 'Create a New Meeting',
        html:
            '<input id="id" type="hidden">' +
            '<input id="agenda" class="swal2-input" placeholder="Meeting Agenda">' +
            '<input id="description" class="swal2-input" placeholder="Description">' +
            '<input type="datetime-local" pattern="[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}" id="start" class="swal2-input" style="padding: 0 4.0em" placeholder="Start Time">' +
            '<input type="datetime-local" pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}&nbsp[0-9]{2}:[0-9]{2}" id="end" class="swal2-input" style="padding: 0 4.0em" placeholder="End Time">' +
            '<input id="allocatedRoomId" class="swal2-input" placeholder="Allocated Room Id">' +
            '<div id="attendee-div"></div>' +
            '<input id="attendee1" class="swal2-input" placeholder="Attendee Email">' +
            '<button type="button" class="btn btn-outline-secondary" onclick="addTextBox()" >+</button>' +
            '',
        focusConfirm: false,
        preConfirm: () => {
            meetingCreate();
        }
    })
}
function addTextBox(){
    var divObj = document.getElementById("attendee-div");
    var node = document.createElement("INPUT");
    node.setAttribute("type", "text");
    node.setAttribute("class", "swal2-input");
    node.setAttribute("placeholder", "Attendee Email");
    node.setAttribute("id","attendee"+count);
    if(count < 6)
    {
        divObj.append(node);
        count++;
    }

}


function meetingCreate() {
    const agenda = document.getElementById("agenda").value;
    const description = document.getElementById("description").value;
    const  start = document.getElementById("start").value.replace('T',' ');
    const end = document.getElementById("end").value.replace('T',' ');

    var allocatedRoomId = document.getElementById("allocatedRoomId");
    if (typeof(allocatedRoomId) != 'undefined' && allocatedRoomId != null)
        allocatedRoomId = allocatedRoomId.value;
    else allocatedRoomId = null;
    const attendeeList = [];
    let temp = 0;
    count --;
    while(temp++<count)
    {
        var a = "attendee"+temp;
        console.log(a);
        attendeeList.push(document.getElementById(a).value);
    }

    const xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://localhost:8080/meeting");
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send(JSON.stringify({
        "agenda": agenda,
        "description": description,
        "host" : login_email,
        "start": start,
        "end": end,
        "allocatedRoomId":allocatedRoomId,
        "attendees":attendeeList

    }));
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 201) {
            const objects = this.responseText;
            Swal.fire({
                icon: 'success',
                title: 'Success',
                showConfirmButton: false,
                timer: 1500,
                text: objects + ' has joined '
            });
            const xhttp = new XMLHttpRequest();
            xhttp.open("GET", "http://localhost:8080/meeting?id="+ login_id);
            loadTable(xhttp);
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
            console.log(this.status);
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!'+ objects['message'] ,
                footer: '<a href="">Why do I have this issue?</a>'
            })
        }
    };
}
