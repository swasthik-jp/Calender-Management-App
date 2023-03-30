//GET ALL OFFICES
function loadTable() {
  const xhttp = new XMLHttpRequest();
  xhttp.open("GET", "http://localhost:8080/meetingrooms");
  xhttp.send();
  console.log("sent")
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      console.log(this.responseText);
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
        trHTML += '<button type="button" class="btn btn-outline-danger" onclick="meetingRoomDelete(' + object['id'] + ')">Del</button></td>';
        trHTML += "</tr>";
      }
      document.getElementById("mytable").innerHTML = trHTML;
    }
  };
}
loadTable();

//CREATE NEW OFFICE
function showMeetingRoomCreateBox() {
  Swal.fire({
    title: 'Add New Meeting Room',
    html:
      '<input id="id" type="hidden">' +
      '<input id="room-name" class="swal2-input" placeholder="Room Name">' +
      '<input id="office-id" class="swal2-input" placeholder="Office ID">' +
      '<input id="capacity" class="swal2-input" placeholder="Room Capacity">' +
      '<label for="is-op">Is Operational</label>' +
      '<label for="is-op-true" class="radio-inline"> <input name="is-op" id="is-op-true" type="radio" value="true"/>True</label>' +
      '<label for="is-op-false" class="radio-inline"> <input name="is-op" id="is-op-false" type="radio" value="false"/>False</label>',


    focusConfirm: false,
    preConfirm: () => {
      meetingRoomCreate();
    }
  })
}

function meetingRoomCreate() {
  const name = document.getElementById("room-name").value;
  const officeId = document.getElementById("office-id").value;
  const capacity = document.getElementById("capacity").value;
  const isOperational = document.querySelector('input[name="is-op"]:checked').value;
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
      console.log("Inside Error Handler : " + objects);
      Swal.fire({
        html: '<p>' + objects['name'] + ' has Been Created '
      });
      loadTable();
    }
    if (this.status == 400) {
      const objects = this.responseText;
      Swal.fire(
        objects
      );
    }
  };
}


//UPDATE OFFICE ADDRESS
function showMeetingRoomEditBox(id) {
  console.log(id);
  const xhttp = new XMLHttpRequest();
  xhttp.open("GET", "http://localhost:8080/meetingroom/" + id);
  xhttp.send();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      const objects = JSON.parse(this.responseText);
      console.log(objects);
      const meetingRoom = objects;
      console.log(meetingRoom);
      var truePlaceHolder = '<label for="is-op-true" class="radio-inline"> <input name="is-op" id="is-op-true" type="radio" value="true" ';
      var falsePlaceHolder = '<label for="is-op-false" class="radio-inline"> <input name="is-op" id="is-op-false" type="radio" value="false"';
      if (meetingRoom['operational'] === true) {
        truePlaceHolder += 'checked="checked"';
      }
      else {
        falsePlaceHolder += 'checked="checked"';
      }
      truePlaceHolder += '/>True</label>';
      falsePlaceHolder += '/>False</label>'
      Swal.fire({
        title: 'Edit MeetingRoom',
        html:
          '<input id="id" type="hidden" value=' + meetingRoom['id'] + '>' +
          '<input id="room-name" class="swal2-input" placeholder="Meeting room name" value="' + meetingRoom['name'] + '">' +
          '<input id="office-id" class="swal2-input" placeholder="Office-Id" value="' + meetingRoom['office']['id'] + '">' +
          '<input id="capacity" class="swal2-input" placeholder="Capacity" value="' + meetingRoom['capacity'] + '">' +
          '<label for="is-op">Is Operational</label>' +
          truePlaceHolder +
          falsePlaceHolder,

        focusConfirm: false,
        preConfirm: () => {
          meetingRoomEdit();
        }
      })
    }
  };
}

function meetingRoomEdit() {
  const id = document.getElementById("id").value;
  const name = document.getElementById("room-name").value;
  const officeId = document.getElementById("office-id").value;
  const capacity = document.getElementById("capacity").value;
  const isOperational = document.querySelector('input[name="is-op"]:checked').value;

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
      //const objects = this.responseText;
      Swal.fire("Success");
      loadTable();
    }
  };
}




function meetingRoomDelete(id) {
  const xhttp = new XMLHttpRequest();
  xhttp.open("DELETE", "http://localhost:8080/meetingroom/" + id);
  xhttp.send();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4) {
      const objects = this.responseText;
      Swal.fire(objects);
      loadTable();
    }
  };
}