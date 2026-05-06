import React from "react";

// Componente interno para los miembros del equipo
const TeamMember = ({ name, role, description }) => (
  <div className="col-md-4 mb-4">
    <div className="card h-100 border-0 shadow-sm text-center p-3">
      <div className="d-flex justify-content-center">
        <div className="rounded-circle bg-secondary mt-3" style={{ width: '100px', height: '100px' }}>
             {/* Aquí podrías poner una etiqueta <img /> más adelante */}
        </div>
      </div>
      <div className="card-body">
        <h5 className="card-title fw-bold">{name}</h5>
        <h6 className="card-subtitle mb-2 text-primary">{role}</h6>
        <p className="card-text text-muted">{description}</p>
      </div>
    </div>
  </div>
);

export default function Nosotros() {
  return (
    <div className="bg-light">
      {/* Hero Section - Banner principal */}
      <section className="py-5 text-center bg-dark text-white shadow-sm">
        <div className="container py-5">
          <h1 className="display-4 fw-bold">Sobre Nosotros</h1>
          <p className="lead">Liderando el camino hacia experiencias web innovadoras.</p>
        </div>
      </section>

      {/* Mission Section - Nuestra Misión */}
      <section className="container py-5">
        <div className="row justify-content-center">
          <div className="col-lg-8 text-center">
            <h2 className="display-6 fw-semibold mb-4 border-bottom pb-2">Nuestra Misión</h2>
            <p className="fs-5 text-secondary leading-relaxed">
              En nuestra empresa, nos dedicamos a potenciar el crecimiento tecnológico 
              brindando herramientas que simplifican lo complejo. Nuestra pasión es 
              crear soluciones que sean tanto potentes como intuitivas para nuestros usuarios.
            </p>
          </div>
        </div>
      </section>

      {/* Team Section - El Equipo */}
      <section className="py-5 bg-white">
        <div className="container">
          <h2 className="text-center mb-5 fw-bold">Nuestro Equipo</h2>
          <div className="row">
            <TeamMember 
              name="Jane Doe" 
              role="Directora Ejecutiva (CEO)" 
              description="Visión estratégica y liderazgo con más de 10 años de experiencia."
            />
            <TeamMember 
              name="John Smith" 
              role="Director de Tecnología (CTO)" 
              description="Experto en arquitectura de software y optimización de sistemas."
            />
            <TeamMember 
              name="Alice Johnson" 
              role="Diseñadora Principal" 
              description="Transformando ideas en interfaces visuales impactantes y funcionales."
            />
          </div>
        </div>
      </section>

      {/* Footer simple para la sección */}
      <footer className="py-4 bg-dark text-white-50 text-center">
        <div className="container">
          <small>&copy; 2026 MyDentistCommerce. Todos los derechos reservados.</small>
        </div>
      </footer>
    </div>
  );
}