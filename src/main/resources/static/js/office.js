//GET ALL OFFICES
function loadTable() {
  const xhttp = new XMLHttpRequest();
  xhttp.open("GET", "http://localhost:8080/offices");
  xhttp.send();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {
      console.log(this.responseText);
      var trHTML = '';
      const objects = JSON.parse(this.responseText);
      for (let object of objects) {
        trHTML += '<tr>';
        trHTML += '<td>' + object['id'] + '</td>';
        trHTML += '<td>' + object['location'] + '</td>';
        trHTML += '<td><button type="button" class="btn btn-outline-secondary" onclick="showOfficeEditBox(' + object['id'] + ')">Edit</button>';
        trHTML += '<button type="button" class="btn btn-outline-danger" onclick="officeDelete(' + object['id'] + ')">Del</button></td>';
        trHTML += "</tr>";
      }
      document.getElementById("mytable").innerHTML = trHTML;
    }
  };
}
loadTable();

//CREATE NEW OFFICE
function showOfficeCreateBox() {
  Swal.fire({
    title: 'Add New Office',
    html:
      '<input id="id" type="hidden">' +
      '<input id="location" class="swal2-input" placeholder="office Address">',
    focusConfirm: false,
    preConfirm: () => {
      officeCreate();
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
    if (this.readyState == 4 && this.status == 200) {
      const objects = JSON.parse(this.responseText);
      Swal.fire(objects);
      loadTable();
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
          officeEdit();
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
      //const objects = this.responseText;
      Swal.fire("Success");
      loadTable();
    }
  };
}




function officeDelete(id) {
  const xhttp = new XMLHttpRequest();
  xhttp.open("DELETE", "http://localhost:8080/office/" + id);
  xhttp.send();
  xhttp.onreadystatechange = function () {
    if (this.readyState == 4) {
      const objects = this.responseText;
      Swal.fire(objects);
      loadTable();
    }
  };
}