import React, { useState } from 'react';
import '../Styles/Administrador.css'
import Productos from "../Components/AdminProduct";
import UsersLoader from '../Components/UsersLoader';

const PRODUCT_URL = "http://localhost:8080/MyMDentalCommerce/products";
const USER_URL = "http://localhost:8080/MyMDentalCommerce/users";

const INITIAL_PRODUCT = {
  codeProduct: "",
  productName: "",
  priceProduct: "",
  costPriceProduct: "",
  stockProduct: "",
  criticProduct: "",
  descriptionProduct: "",
  nameDepartment: "",
};

const INITIAL_USER = {
  name: "",
  lastName: "",
  email: "",
  password: "",
  password2: "",
  phone: "",
}

const TITLES = [
  {id : "registrar", label: "Gestión de productos"},
  {id : "actualizar", label: "Gestión de productos"},
  {id : "eliminar", label: "Gestión de productos"},
  { id: "user", label: "Gestión de usuarios" },
  {id: "userList", label: "Gestion de usuarios" },
  {id: "userEdit", label: "Gestión de usuarios" },
  {id: "userDelete", label: "Gestión de usuarios" }

];

const TABS = [
  { id: "registrar", label: "Registrar" },
  { id: "actualizar", label: "Actualizar" },
  { id: "eliminar", label: "Eliminar" },
  { id: "user", label: "Registrar Usuario" },
  { id: "userEdit", label: "Editar Usuario" },
  { id: "userDelete", label: "Eliminar Usuario" },
  { id: "userList", label: "Lista de Usuarios" }
  
];

function UserField({ label, type = "text", value, onChange, placeholder, required }) {
  const Tag = type === "textarea" ? "textarea" : "input";
  return (
    <div className="field-container">
      <label className="field-label">{label}</label>
      <Tag
        type={type !== "textarea" ? type : undefined}
        className={`field-input ${type === "textarea" ? "field-textarea" : ""}`}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
      />
    </div>
  );
}

function Field({ label, type = "text", value, onChange, placeholder, required, full }) {
  const Tag = type === "textarea" ? "textarea" : "input";
  return (
    <div className={`field-container ${full ? "field-full-width" : ""}`}>
      <label className="field-label">{label}</label>
      <Tag
        type={type !== "textarea" ? type : undefined}
        className={`field-input ${type === "textarea" ? "field-textarea" : ""}`}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
      />
    </div>
  );
}

function PrimaryButton({ children, onClick }) {
  return (
    <button className="primary-button" onClick={onClick}>
      {children}
    </button>
  );
}

export default function Administrador() {
  const [activeTab, setActiveTab] = useState("registrar");
  const [product, setProduct] = useState({ ...INITIAL_PRODUCT });
  const [updateProduct, setUpdateProduct] = useState({ ...INITIAL_PRODUCT });
  const [deleteProductName, setDeleteProductName] = useState("");
  const [users, setUsers] = useState([]);

  const setField = (setter) => (key) => (e) => setter((prev) => ({ ...prev, [key]: e.target.value }));

  const handleListar = () => {
    fetch(`${USER_URL}/getUsers`,{
      method: "GET",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(product),
    })
      .then((res) => {
        if (res.ok) {
          alert("Producto registrado exitosamente");
          setProduct({ ...INITIAL_PRODUCT });
        } else {
          alert("Error al registrar el producto");
        }
      })
      .catch(() => alert("Error de conexión con el servidor"));
    };

  const handleRegistrar = () => {
    fetch(`${PRODUCT_URL}/saveProduct`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(product),
    })
      .then((res) => {
        if (res.ok) {
          alert("Producto registrado exitosamente");
          setProduct({ ...INITIAL_PRODUCT });
        } else {
          alert("Error al registrar el producto");
        }
      })
      .catch(() => alert("Error de conexión con el servidor"));
  };

  const handleActualizar = () => {
    fetch(`${PRODUCT_URL}/editProduct/${updateProduct.productName}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(updateProduct),
    })
      .then((res) => {
        if (res.ok) {
          alert("Producto actualizado exitosamente");
          setUpdateProduct({ ...INITIAL_PRODUCT });
        } else {
          alert("Error al actualizar el producto");
        }
      })
      .catch(() => alert("Error de conexión con el servidor"));
  };

  const handleEliminar = () => {
    if (!deleteProductName) return alert("Ingresa el nombre del producto.");
    fetch(`${PRODUCT_URL}/deleteProduct/${deleteProductName}`, { method: "DELETE" })
      .then((res) => {
        if (res.ok) {
          alert("Producto eliminado exitosamente");
          setDeleteProductName("");
        } else {
          alert("Error al eliminar el producto");
        }
      })
      .catch(() => alert("Error de conexión con el servidor"));
  };

  const f = setField(setProduct);
  const u = setField(setUpdateProduct);

  return (
    <div className="admin-container">
      {/* Header */}
      <div className="admin-header">
        <p className="admin-subtitle">Panel de administración</p>
        <h1 className="admin-title">{TITLES.find((title) => title.id === activeTab)?.label}</h1>
      </div>

      {/* Tabs */}
      <div className="tabs-container">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`tab-button ${activeTab === tab.id ? "active" : ""}`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Tab: Registrar */}
      {activeTab === "registrar" && (
        <div>
          <h1 className="admin-title">Registrar nuevo producto</h1>
          <div className="product-grid">
            <Field label="Código del producto" value={product.codeProduct} onChange={f("codeProduct")} placeholder="ej. PROD-001" required />
            <Field label="Nombre del producto" value={product.productName} onChange={f("productName")} placeholder="Nombre" required />
            <Field label="Precio de venta" type="number" value={product.priceProduct} onChange={f("priceProduct")} placeholder="0" required />
            <Field label="Precio de compra" type="number" value={product.costPriceProduct} onChange={f("costPriceProduct")} placeholder="0" required />
            <Field label="Stock inicial" type="number" value={product.stockProduct} onChange={f("stockProduct")} placeholder="0" required />
            <Field label="Stock crítico" type="number" value={product.criticProduct} onChange={f("criticProduct")} placeholder="0" />
            <Field label="Categoría" value={product.nameDepartment} onChange={f("nameDepartment")} placeholder="ej. Ortodoncia" full />
            <Field label="Descripción" type="textarea" value={product.descriptionProduct} onChange={f("descriptionProduct")} placeholder="Descripción del producto..." full />
          </div>
          <div className="button-container">
            <PrimaryButton onClick={handleRegistrar}>Registrar producto</PrimaryButton>
          </div>
        </div>
      )}

      {/* Tab: Actualizar */}
      {activeTab === "actualizar" && (
        
        <div>
          <h1 className="admin-title">Actualizar producto existente</h1>
          <div className="product-grid">
            <Field label="Nombre del producto a actualizar" value={updateProduct.productName} onChange={u("productName")} placeholder="Nombre exacto del producto" required full />
            <Field label="Nuevo código" value={updateProduct.codeProduct} onChange={u("codeProduct")} placeholder="ej. PROD-002" />
            <Field label="Nueva categoría" value={updateProduct.nameDepartment} onChange={u("nameDepartment")} placeholder="ej. Endodoncia" />
            <Field label="Nuevo precio de venta" type="number" value={updateProduct.priceProduct} onChange={u("priceProduct")} placeholder="0" />
            <Field label="Nuevo precio de compra" type="number" value={updateProduct.costPriceProduct} onChange={u("costPriceProduct")} placeholder="0" />
            <Field label="Nuevo stock inicial" type="number" value={updateProduct.stockProduct} onChange={u("stockProduct")} placeholder="0" />
            <Field label="Nuevo stock crítico" type="number" value={updateProduct.criticProduct} onChange={u("criticProduct")} placeholder="0" />
            <Field label="Nueva descripción" type="textarea" value={updateProduct.descriptionProduct} onChange={u("descriptionProduct")} placeholder="Nueva descripción..." full />
          </div>
          <div className="button-container">
            <PrimaryButton onClick={handleActualizar}>Actualizar producto</PrimaryButton>
          </div>
          <div>
            
          </div>
        </div>
      )}

      {/* Tab: Eliminar */}
      {activeTab === "eliminar" && (

        <div className="danger-zone">
          <h1 className="admin-title">Eliminar producto</h1>
          <p className="danger-label">Area de peligro</p>
          <p className="danger-text">
            acción irreversible. Ingresa el nombre exacto del producto que deseas eliminar.
          </p>
          <div className="delete-action-row">
            <div style={{ flex: 1 }}>
              <Field
                label="Nombre del producto"
                value={deleteProductName}
                onChange={(e) => setDeleteProductName(e.target.value)}
                placeholder="Nombre exacto del producto"
                required
              />
            </div>
            <button className="delete-button" onClick={handleEliminar}>
              Eliminar producto
            </button>
          </div>
        </div>
      )}

{/*Tab : Usuarios*/}
{activeTab === "user" && (
  <div>
    <h1 className="admin-title">Registro de usuarios</h1>
    <div>
      <UserField label="Nombre" value={INITIAL_USER.name} onChange={() => {}} placeholder="Nombre" required />
      <UserField label="Apellido" value={INITIAL_USER.lastName} onChange={() => {}} placeholder="Apellido" required />
      <UserField label="Email" type="email" value={INITIAL_USER.email} onChange={() => {}} placeholder="Email" required />
    </div>
  </div>
)}


{activeTab === "userEdit" && (
  <div>
    <h1 className="admin-title">Editar usuario</h1>
  </div>
)}
{activeTab === "userList" && (
  <div>
    <h1 className="admin-title">Lista de usuarios</h1>
    <div>
      <table>
        <thead>
          <tr>
            <th>Usuario</th>
            <th>Apellido</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          {/* Render user rows here */}
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.name}</td>
              <td>{user.lastName}</td>
              <td>{user.email}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
)}
<div>
    <usersLoader entity='Usuarios'/>
</div>
    </div>
  );
}