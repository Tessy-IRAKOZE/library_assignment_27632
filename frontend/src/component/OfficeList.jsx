import React, { useState, useEffect } from 'react'
import axios from 'axios'
import LibraryButton from './LibraryButton'

function OfficeList() {
    const [offices, setOffices] = useState([])
    const [search, setSearch] = useState("")

    async function loadOffices(){
        const response = await axios.get("http://localhost:8082/api/office/all")
        setOffices(response.data)
    }

    useEffect(() => {
        loadOffices()
    }, []
)
   const filteredOffices = offices.filter(office =>
   office.name.toLowerCase().includes(search.toLowerCase()) ||
   String(office.officeNumber).includes(search)
   )

   async function deleteOffice(officeNumber){
    const response = await axios.delete(`http://localhost:8082/api/office/deleteByNumber/${officeNumber}`,
       
        {
            validateStatus: () => true
        } 
    )
    if (response.status == 200){
        alert("office deleted successfully")
        loadOffices()
    }else{
        alert("office not deleted")
    }    
   }
  return (
    <div>
       <h2>Available Offices</h2>
       <input type ='text' placeholder='search by name or number' onChange= {e=>setSearch(e.target.value)}/>
       <table border="1">
        <thead>
            <tr>
                <th>Office ID</th>
                <th>Office Name</th>
                <th>Office Number</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            {
                filteredOffices.map(office =>
                    <tr key={office.id}>
                        <td>{office.id}</td>
                        <td>{office.name}</td>
                        <td>{office.officeNumber}</td>
                        <td>
                            <LibraryButton buttonLabel= "Update" />
                            <LibraryButton buttonLabel ="Delete" onClickButton={() => deleteOffice(office.officeNumber)} /> 
                         </td>
                    </tr>
                )
            }
        </tbody>
       </table>
    </div>
  )
}

export default OfficeList