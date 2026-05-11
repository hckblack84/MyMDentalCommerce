import { useParams } from 'react-router-dom';
import Productos from '../Components/Productos';
import "../Styles/Categoria.css"

export default function Categoria() {

  const { nameDepartment } = useParams();
  const nombreCategoria = decodeURIComponent(nameDepartment);

  console.log("Categoria:", nombreCategoria);

  return (
    <>
      <h2 className='Titulo' class="text-center">{nombreCategoria}</h2>
      <Productos isFiltered={true} filter={nameDepartment} />
    </>
  );
}