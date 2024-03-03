const express = require("express");
const fs = require("fs");
var bodyParser = require("body-parser");
const path = require("path");
const app = express();
app.use(express.static("public"));
app.use(bodyParser.json());

app.get("/get_patients", (req, res) => {
  let path = "data/patient.json"
  let data = fs.readFileSync(path)
  var dataObject = JSON.parse(data)
  console.log(dataObject)
  res.send(dataObject)
});

app.get("/get_patient_medication", (req, res) => {
  let path = "data/medication.json"
  let data = fs.readFileSync(path)
  var dataObject = JSON.parse(data)
  let patientId = req.query.patient_id;

  let filteredMedication = dataObject.filter(medication => medication.patient_id === patientId);
  
  console.log(filteredMedication);
  res.send(filteredMedication)
});

app.get("/get_patient_heartrate", (req, res) => {
  let path = "data/heartrate.json"
  let data = fs.readFileSync(path)
  var dataObject = JSON.parse(data)
  let patientId = req.query.patient_id;

  let filteredHeartrate = dataObject.filter(heartrate => heartrate.patient_id === patientId);
  
  console.log(filteredHeartrate);
  res.send(filteredHeartrate)
});

app.get("/get_patient_pressure", (req, res) => {
  let path = "data/pressure.json"
  let data = fs.readFileSync(path)
  var dataObject = JSON.parse(data)
  let patientId = req.query.patient_id;

  let filteredPressure = dataObject.filter(pressure => pressure.patient_id === patientId);
  
  console.log(filteredPressure);
  res.send(filteredPressure)
});

app.get("/get_patient_surgeries", (req, res) => {
  let path = "data/surgery.json"
  let data = fs.readFileSync(path)
  var dataObject = JSON.parse(data)
  let patientId = req.query.patient_id;

  let filteredSurgery = dataObject.filter(surgery => surgery.patient_id === patientId);
  
  console.log(filteredSurgery);
  res.send(filteredSurgery)
});

app.get("/get_patient_medical_history", (req, res) => {
  // todo implement
  // let path = "data/medical_history.json" 
  // let data = fs.readFileSync(path)
  // var dataObject = JSON.parse(data)
  // let patientId = req.query.patient_id;

  //let filteredSurgery = dataObject.filter(surgery => surgery.patient_id === patientId);
  
  console.log(filteredSurgery);
  res.send(Array())
});

app.listen(8090, () => {
  console.log("SERVER STARTED\n","Server running at http://localhost:8090/");
});

app.use(
  bodyParser.urlencoded({
    extended: true,
  })
);
