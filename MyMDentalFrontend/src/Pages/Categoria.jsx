import { useParams } from 'react-router-dom';
import Productos from '../Components/Productos';
import "../Styles/Categoria.css"

export default function Categoria() {

  const { nameDepartment } = useParams();
  const nombreCategoria = decodeURIComponent(nameDepartment);

  const urlBack = `http://localhost:8080/MyMDentalCommerce/products/filterAdminProducts/${nombreCategoria}`;

  console.log("Categoria:", nombreCategoria);
  console.log("URL:", urlBack);

  return (
    <>
      <h2 className='Titulo' class="text-center">{nombreCategoria}</h2>
      <Productos urlBack={urlBack} />
    </>
  );
}