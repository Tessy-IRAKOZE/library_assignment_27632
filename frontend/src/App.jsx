import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import LibraryFooter from './component/LibraryFooter'
import LibraryButton from './component/LibraryButton'
import LibraryBooks from './component/LibraryBooks'
import LibraryLogin from './component/LibraryLogin'
import Office from './component/Office'
import OfficeList from './component/OfficeList'
function App() {
  const libraryHeader = "LIBRARY mangement system"

  return (
    <div>
      <header>
      <h1>{libraryHeader}</h1>
      <p> This is a libary System that will help you</p>
      </header>
      <main>
        {/* <LibraryBooks />
        <LibraryButton />
        <LibraryFooter />
        <LibraryLogin /> */}
        <Office />
        
        <OfficeList />
      </main>
    </div>

  );
}


export default App
