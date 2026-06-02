import React, { useState, useEffect, useRef } from 'react';
import '../Styles/Administrador.css';

const API_PRODUCTS = import.meta.env.VITE_API_PRODUCTS;
const API_DEPARTMENTS = import.meta.env.VITE_API_DEPARTMENTS;
const API_USERS = import.meta.env.VITE_API_GetUsers;
const API_USER_REGISTER = import.meta.env.VITE_API_USER_REGISTER;
const API_UPDATE_USER = import.meta.env.VITE_API_UPDATE_USER;
const API_DELETE_USER = import.meta.env.VITE_API_DELETE_USER;

const INITIAL_PRODUCT = {
  codeProduct: "",
  productName: "",
  priceProduct: "",
  costPriceProduct: "",
  stockProduct: "",
  criticProduct: "",
  descriptionProduct: "",
  nameDepartment: "",
  imageProduct: "",
  activeProduct: true,
};

const INITIAL_USER = {
  nameUser: "",
  surnameUser: "",
  emailUser: "",
  passwordUser: "",
  cellphoneUser: "",
  role: "CLIENT",
};


const ROLES = ["CLIENT", "ADMINISTRATOR", "WORKER"];

const PRODUCT_TABS = [
  { id: "registrar", label: "Registrar" },
  { id: "actualizar", label: "Actualizar" },
  { id: "eliminar", label: "Eliminar" },
];

const USER_TABS = [
  { id: "registrar", label: "Registrar" },
  { id: "actualizar", label: "Actualizar" },
  { id: "eliminar", label: "Eliminar" },
  { id: "listar", label: "Listar" },
];

function Field({ label, type = "text", value, onChange, placeholder, required, full, options }) {
  const Tag = type === "textarea" ? "textarea" : type === "select" ? "select" : "input";
  return (
    <div className={`field-container ${full ? "field-full-width" : ""}`}>
      <label className="field-label">{label}</label>
      {Tag === "select" ? (
        <select className="field-input" value={value} onChange={onChange} required={required}>
          <option value="">-- Seleccionar --</option>
          {options?.map(opt => (
            <option key={opt.value} value={opt.value}>{opt.label}</option>
          ))}
        </select>
      ) : (
        <Tag
          type={type !== "textarea" && type !== "select" ? type : undefined}
          className={`field-input ${type === "textarea" ? "field-textarea" : ""}`}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          required={required}
        />
      )}
    </div>
  );
}

// ── NEW: Image Upload Component ──────────────────────────────────────────────
function ImageUpload({ label = "Imagen del producto", value, onChange, full }) {
  const inputRef = useRef(null);

  const handleFile = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (ev) => onChange(ev.target.result);
    reader.readAsDataURL(file);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    const file = e.dataTransfer.files[0];
    if (!file || !file.type.startsWith('image/')) return;
    const reader = new FileReader();
    reader.onload = (ev) => onChange(ev.target.result);
    reader.readAsDataURL(file);
  };

  const handleDragOver = (e) => e.preventDefault();

  const handleRemove = (e) => {
    e.stopPropagation();
    onChange("");
    if (inputRef.current) inputRef.current.value = "";
  };

  return (
    <div className={`field-container ${full ? "field-full-width" : ""}`}>
      <label className="field-label">{label}</label>
      <div
        className={`image-upload-zone ${value ? "has-image" : ""}`}
        onClick={() => !value && inputRef.current?.click()}
        onDrop={handleDrop}
        onDragOver={handleDragOver}
      >
        {value ? (
          <div className="image-upload-preview">
            <img src={value} alt="Vista previa" className="image-preview-img" />
            <div className="image-upload-overlay">
              <button
                type="button"
                className="image-change-btn"
                onClick={() => inputRef.current?.click()}
              >
                Cambiar imagen
              </button>
              <button
                type="button"
                className="image-remove-btn"
                onClick={handleRemove}
              >
                Eliminar
              </button>
            </div>
          </div>
        ) : (
          <div className="image-upload-placeholder">
           <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
              <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
              <circle cx="8.5" cy="8.5" r="1.5"/>
              <polyline points="21 15 16 10 5 21"/>
            </svg>
            <p className="image-upload-text">Arrastra una imagen o haz clic para seleccionar</p>
            <p className="image-upload-hint">PNG, JPG, WEBP — máx. 5 MB</p>
          </div>
        )}
        <input
          ref={inputRef}
          type="file"
          accept="image/*"
          style={{ display: "none" }}
          onChange={handleFile}
        />
      </div>
    </div>
  );
}
// ────────────────────────────────────────────────────────────────────────────

function PrimaryButton({ children, onClick, disabled }) {
  return (
    <button className="primary-button" onClick={onClick} disabled={disabled}>
      {children}
    </button>
  );
}

export default function Administrador() {
  const [section, setSection] = useState("productos");
  const [activeTab, setActiveTab] = useState("registrar");
  const [departments, setDepartments] = useState([]);
  const [feedback, setFeedback] = useState(null);

  // Product state
  const [product, setProduct] = useState({ ...INITIAL_PRODUCT });
  const [updateProduct, setUpdateProduct] = useState({ ...INITIAL_PRODUCT });
  const [deleteProductName, setDeleteProductName] = useState("");
  const [productList, setProductList] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [searchCategory, setSearchCategory] = useState("");

  // User state
  const [user, setUser] = useState({ ...INITIAL_USER });
  const [updateUser, setUpdateUser] = useState({ ...INITIAL_USER });
  const [deleteUserEmail, setDeleteUserEmail] = useState("");
  const [usersList, setUsersList] = useState([]);

  useEffect(() => {
    fetch(API_DEPARTMENTS)
      .then(res => res.json())
      .then(data => setDepartments(data))
      .catch(() => console.error("Error cargando departamentos"));
  }, []);

  const showFeedback = (msg, type) => {
    setFeedback({ msg, type });
    setTimeout(() => setFeedback(null), 4000);
  };

  const depOptions = departments.map(d => ({
    value: d.nameDepartment,
    label: d.nameDepartment,
  }));

  const setField = (setter) => (key) => (e) =>
    setter((prev) => ({ ...prev, [key]: e.target.value }));

  // Product handlers
  const handleRegistrarProducto = async () => {
    if (!product.imageProduct) {
      alert("Por favor, selecciona una imagen para el producto.");
      return;
    }

    try {
      const formData = new FormData();
      const { imageProduct, ...dtoProductAdmin } = product;

      const productBlob = new Blob([JSON.stringify(dtoProductAdmin)], {
        type: 'application/json'
      });
      formData.append('product', productBlob);

      const response = await fetch(imageProduct);
      const imageBlob = await response.blob();
      
      formData.append('image', imageBlob, 'imagen_producto.jpg');

      const res = await fetch(`${API_PRODUCTS}/saveProduct`, {
        method: "POST",
        body: formData,
        credentials: "include", 
      });


      const text = await res.text();

      console.log("STATUS:", res.status);
      console.log("RESPUESTA:", text);

      if (res.ok) {
        showFeedback("Producto registrado exitosamente", "success");
        setProduct({ ...INITIAL_PRODUCT });
      } else {
        showFeedback("Error al registrar el producto", "error");
      }
    } catch (error) {
      console.error(error);
      showFeedback("Error de conexión con el servidor", "error");
    }
  };

const handleActualizarProducto = async () => {
  try {

    const formData = new FormData();

    const { imageProduct, ...dtoProductAdmin } = updateProduct;

    const productBlob = new Blob(
      [JSON.stringify(dtoProductAdmin)],
      {
        type: "application/json"
      }
    );

    formData.append("product", productBlob);

    if (imageProduct) {
      const response = await fetch(imageProduct);
      const imageBlob = await response.blob();
      formData.append(
        "image",
        imageBlob,
        "imagen_producto.jpg"
      );
    }

    const res = await fetch(
      `${API_PRODUCTS}/editProduct/${updateProduct.productName}`,
      {
        method: "PUT",
        body: formData,
        credentials: "include"
      }
    );

    const text = await res.text();

    console.log("STATUS:", res.status);
    console.log("RESPUESTA:", text);

    if (res.ok) {
      showFeedback(
        "Producto actualizado exitosamente",
        "success"
      );

      setUpdateProduct({
        ...INITIAL_PRODUCT
      });

      fetchProductList();

    } else {
      showFeedback(
        "Error al actualizar el producto",
        "error"
      );
    }

  } catch(error) {

    console.error(error);

    showFeedback(
      "Error de conexión con el servidor",
      "error"
    );
  }
};

  const handleEliminarProducto = () => {
    if (!deleteProductName) return showFeedback("Ingresa el nombre del producto.", "error");
    fetch(`${API_PRODUCTS}/deleteProduct/${deleteProductName}`, { method: "DELETE", credentials: "include" })
      .then(res => {
        if (res.ok) {
          showFeedback("Producto eliminado exitosamente", "success");
          setDeleteProductName("");
        } else {
          showFeedback("Error al eliminar el producto", "error");
        }
      })
      .catch(() => showFeedback("Error de conexión con el servidor", "error"));
  };

  const fetchProductList = () => {
    fetch(`${API_PRODUCTS}/getProducts`)
      .then(res => res.json())
      .then(data => setProductList(data))
      .catch(() => showFeedback("Error al cargar productos", "error"));
  };

  const handleRegistrarUsuario = () => {
    fetch(API_USER_REGISTER, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(user),
    })
      .then(res => {
        if (res.ok) {
          showFeedback("Usuario registrado exitosamente", "success");
          setUser({ ...INITIAL_USER });
        } else {
          showFeedback("Error al registrar el usuario", "error");
        }
      })
      .catch(() => showFeedback("Error de conexión con el servidor", "error"));
  };

  const handleActualizarUsuario = () => {
    fetch(`${API_UPDATE_USER}/${updateUser.emailUser}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify(updateUser),
    })
      .then(res => {
        if (res.ok) {
          showFeedback("Usuario actualizado exitosamente", "success");
          setUpdateUser({ ...INITIAL_USER });
        } else {
          showFeedback("Error al actualizar el usuario", "error");
        }
      })
      .catch(() => showFeedback("Error de conexión con el servidor", "error"));
  };

  const handleEliminarUsuario = () => {
    if (!deleteUserEmail) return showFeedback("Ingresa el email del usuario.", "error");
    fetch(`${API_DELETE_USER}/${deleteUserEmail}`, { method: "DELETE", credentials: "include" })
      .then(res => {
        if (res.ok) {
          showFeedback("Usuario eliminado exitosamente", "success");
          setDeleteUserEmail("");
        } else {
          showFeedback("Error al eliminar el usuario", "error");
        }
      })
      .catch(() => showFeedback("Error de conexión con el servidor", "error"));
  };

  const handleListarUsuarios = () => {
    fetch(API_USERS)
      .then(res => res.json())
      .then(data => setUsersList(data))
      .catch(() => showFeedback("Error al cargar usuarios", "error"));
  };

  useEffect(() => {
    if (section === "usuarios" && activeTab === "listar") {
      handleListarUsuarios();
    }
  }, [section, activeTab]);

  const pf = setField(setProduct);
  const pu = setField(setUpdateProduct);
  const uf = setField(setUser);
  const uu = setField(setUpdateUser);

  return (
    <div className="admin-container">
      {/* Feedback */}
      {feedback && (
        <div className={`feedback-msg ${feedback.type}`}>{feedback.msg}</div>
      )}

      {/* Header */}
      <div className="admin-header">
        <p className="admin-subtitle">Panel de administración</p>
        <h1 className="admin-title">Gestión</h1>
      </div>

      {/* Section tabs */}
      <div className="section-tabs">
        <button
          className={`section-tab ${section === "productos" ? "active" : ""}`}
          onClick={() => { setSection("productos"); setActiveTab("registrar"); }}
        >
          Productos
        </button>
        <button
          className={`section-tab ${section === "usuarios" ? "active" : ""}`}
          onClick={() => { setSection("usuarios"); setActiveTab("registrar"); }}
        >
          Usuarios
        </button>
      </div>

      {/* Product Section */}
      {section === "productos" && (
        <>
          <div className="tabs-container">
            {PRODUCT_TABS.map(tab => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`tab-button ${activeTab === tab.id ? "active" : ""}`}
              >
                {tab.label}
              </button>
            ))}
          </div>

          {/* Registrar Producto */}
          {activeTab === "registrar" && (
            <div>
              <div className="product-grid">
                <Field label="Código" value={product.codeProduct} onChange={pf("codeProduct")} placeholder="ej. PROD-001" required />
                <Field label="Nombre" value={product.productName} onChange={pf("productName")} placeholder="Nombre" required />
                <Field label="Precio venta" type="number" value={product.priceProduct} onChange={pf("priceProduct")} placeholder="0" required />
                <Field label="Precio compra" type="number" value={product.costPriceProduct} onChange={pf("costPriceProduct")} placeholder="0" required />
                <Field label="Stock" type="number" value={product.stockProduct} onChange={pf("stockProduct")} placeholder="0" required />
                <Field label="Stock crítico" type="number" value={product.criticProduct} onChange={pf("criticProduct")} placeholder="0" />
                <Field label="Categoría" type="select" value={product.nameDepartment} onChange={pf("nameDepartment")} options={depOptions} full />
                <Field label="Descripción" type="textarea" value={product.descriptionProduct} onChange={pf("descriptionProduct")} placeholder="Descripción..." full />
                <ImageUpload
                  label="Imagen del producto"
                  value={product.imageProduct}
                  onChange={(base64) => setProduct(prev => ({ ...prev, imageProduct: base64 }))}
                  full
                />
              </div>
              <div className="button-container">
                <PrimaryButton onClick={handleRegistrarProducto}>Registrar producto</PrimaryButton>
              </div>
            </div>
          )}

          {/* Actualizar Producto */}
          {activeTab === "actualizar" && (
            <div>
              <div className="product-grid">
                <Field label="Nombre (exacto)" value={updateProduct.productName} onChange={pu("productName")} placeholder="Nombre exacto" required full />
                <Field label="Nuevo código" value={updateProduct.codeProduct} onChange={pu("codeProduct")} placeholder="ej. PROD-002" />
                <Field label="Nueva categoría" type="select" value={updateProduct.nameDepartment} onChange={pu("nameDepartment")} options={depOptions} />
                <Field label="Nuevo precio venta" type="number" value={updateProduct.priceProduct} onChange={pu("priceProduct")} placeholder="0" />
                <Field label="Nuevo precio compra" type="number" value={updateProduct.costPriceProduct} onChange={pu("costPriceProduct")} placeholder="0" />
                <Field label="Nuevo stock" type="number" value={updateProduct.stockProduct} onChange={pu("stockProduct")} placeholder="0" />
                <Field label="Nuevo stock crítico" type="number" value={updateProduct.criticProduct} onChange={pu("criticProduct")} placeholder="0" />
                <Field label="Nueva descripción" type="textarea" value={updateProduct.descriptionProduct} onChange={pu("descriptionProduct")} placeholder="Nueva descripción..." full />
                {/* ── IMAGE UPLOAD ── */}
                <ImageUpload
                  label="Nueva imagen del producto"
                  value={updateProduct.imageProduct}
                  onChange={(base64) => setUpdateProduct(prev => ({ ...prev, imageProduct: base64 }))}
                  full
                />
              </div>
              <div className="button-container">
                <PrimaryButton onClick={handleActualizarProducto}>Actualizar producto</PrimaryButton>
              </div>
            </div>
          )}

          {/* Eliminar Producto */}
          {activeTab === "eliminar" && (
            <div className="danger-zone">
              <p className="danger-label">Zona de peligro</p>
              <p className="danger-text">Ésta acción es irreversible. Ingresa el nombre exacto del producto.</p>
              <div className="delete-action-row">
                <div style={{ flex: 1 }}>
                  <Field
                    label="Nombre del producto"
                    value={deleteProductName}
                    onChange={(e) => setDeleteProductName(e.target.value)}
                    placeholder="Nombre exacto"
                    required
                  />
                </div>
                <button className="delete-button" onClick={handleEliminarProducto}>
                  Eliminar producto
                </button>
              </div>
            </div>
          )}
        </>
      )}

      {/* User Section */}
      {section === "usuarios" && (
        <>
          <div className="tabs-container">
            {USER_TABS.map(tab => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`tab-button ${activeTab === tab.id ? "active" : ""}`}
              >
                {tab.label}
              </button>
            ))}
          </div>

          {/* Registrar Usuario */}
          {activeTab === "registrar" && (
            <div>
              <div className="user-grid">
                <Field label="Nombre" value={user.nameUser} onChange={uf("nameUser")} placeholder="Nombre" required />
                <Field label="Apellido" value={user.surnameUser} onChange={uf("surnameUser")} placeholder="Apellido" required />
                <Field label="Email" type="email" value={user.emailUser} onChange={uf("emailUser")} placeholder="correo@ejemplo.com" required full />
                <Field label="Contraseña" type="password" value={user.passwordUser} onChange={uf("passwordUser")} placeholder="••••••" required />
                <Field label="Teléfono" type="number" value={user.cellphoneUser} onChange={uf("cellphoneUser")} placeholder="9 dígitos" required />
                <Field label="Rol" type="select" value={user.role} onChange={uf("role")} options={ROLES.map(r => ({ value: r, label: r }))} />
              </div>
              <div className="button-container">
                <PrimaryButton onClick={handleRegistrarUsuario}>Registrar usuario</PrimaryButton>
              </div>
            </div>
          )}

          {/* Actualizar Usuario */}
          {activeTab === "actualizar" && (
            <div>
              <div className="user-grid">
                <Field label="Email del usuario a actualizar" type="email" value={updateUser.emailUser} onChange={uu("emailUser")} placeholder="correo@ejemplo.com" required full />
                <Field label="Nuevo nombre" value={updateUser.nameUser} onChange={uu("nameUser")} placeholder="Nombre" />
                <Field label="Nuevo apellido" value={updateUser.surnameUser} onChange={uu("surnameUser")} placeholder="Apellido" />
                <Field label="Nueva contraseña" type="password" value={updateUser.passwordUser} onChange={uu("passwordUser")} placeholder="••••••" />
                <Field label="Nuevo teléfono" type="number" value={updateUser.cellphoneUser} onChange={uu("cellphoneUser")} placeholder="9 dígitos" />
                <Field label="Nuevo rol" type="select" value={updateUser.role} onChange={uu("role")} options={ROLES.map(r => ({ value: r, label: r }))} />
              </div>
              <div className="button-container">
                <PrimaryButton onClick={handleActualizarUsuario}>Actualizar usuario</PrimaryButton>
              </div>
            </div>
          )}

          {/* Eliminar Usuario */}
          {activeTab === "eliminar" && (
            <div className="danger-zone">
              <p className="danger-label">Zona de peligro</p>
              <p className="danger-text">Ésta acción es irreversible. Ingresa el email exacto del usuario a eliminar.</p>
              <div className="delete-action-row">
                <div style={{ flex: 1 }}>
                  <Field
                    label="Email del usuario"
                    type="email"
                    value={deleteUserEmail}
                    onChange={(e) => setDeleteUserEmail(e.target.value)}
                    placeholder="correo@ejemplo.com"
                    required
                  />
                </div>
                <button className="delete-button" onClick={handleEliminarUsuario}>
                  Eliminar usuario
                </button>
              </div>
            </div>
          )}

          {/* Listar Usuarios */}
          {activeTab === "listar" && (
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
                <p style={{ fontSize: 14, color: '#666' }}>{usersList.length} usuario(s) encontrado(s)</p>
                <button className="primary-button" onClick={handleListarUsuarios}>
                  Recargar
                </button>
              </div>
              {usersList.length === 0 ? (
                <p style={{ color: '#888', textAlign: 'center' }}>No hay usuarios para mostrar.</p>
              ) : (
                <div className="user-list">
                  {usersList.map((u, i) => (
                    <div className="user-card" key={u.emailUser ?? i}>
                      <h5>{u.nameUser} {u.surnameUser}</h5>
                      <p><strong>Email:</strong> {u.emailUser}</p>
                      <p><strong>Teléfono:</strong> {u.cellphoneUser}</p>
                      <p><strong>Rol:</strong> {u.role}</p>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
}