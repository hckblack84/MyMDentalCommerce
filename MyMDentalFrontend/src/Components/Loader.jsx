import React from 'react'
import '../Styles/Loader.css'

export default function Loader({entity=''}) {
  return (
    <>
    <div style={{display:'flex', 
        flexDirection:'column',
        alignItems:'center',
        justifyContent:'center'
        }}>
        <div className="loader"></div>
        <div>Cargando <strong>{entity}</strong></div>
    </div>
    </>
  )
}
