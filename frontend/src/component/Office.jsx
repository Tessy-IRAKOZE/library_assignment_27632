import React, { useState } from 'react'
import LibraryButton from './LibraryButton'
import axios from 'axios'


function Office() {

    const [name, setName] = useState("")
    const [officeNumber, setOfficeNumber] = useState("")
    const [offNumber,setOffNumber] = useState("")

    async function saveOffice(){
        const response = await axios.post("http://localhost:8082/api/office/save",
        {
           name: name,
           officeNumber: officeNumber
        }
    )
    if(response.status == 200){
        alert("The office is saved successfully")
    }else{
        alert("office not saved")
    }
          
    }
       async function deleteOffice(){
       const response = await axios.delete(`http://localhost:8082/api/office/deleteByNumber/${offNumber}`,
       {
          validateStatus: () => true
       }
    )
       if(response.status == 200){
        alert("office deleted successfully")
       }else{
        alert("office not deleted")
       }
    }



  return (
    <div>
        <h1>SAVE OFFICE</h1>
        <label>Office Name:</label> <input type='text' onChange={e=>setName(e.target.value)}/>
        <label>Office Number:</label> <input type='text' onChange={e=>setOfficeNumber(e.target.value)}/>
        <LibraryButton buttonLabel="SAVE" onClickButton={saveOffice}/>
        <h2>Delete Office</h2>
        <input type='text' onChange={e=>setOffNumber(e.target.value)}/>
        <LibraryButton buttonLabel="DELETE" onClickButton={deleteOffice}/>


    </div>
  )
}

export default Office