import React from "react";
import "../Styles/Nosotros.css";

const FeatureCard = ({ title, description }) => (

  <div className="col-lg-4 col-md-6 mb-4">
    <div className="feature-card h-100">
     <div className="feature-line"></div>
      <h4>{title}</h4>
      <p>{description}</p>
    </div>
  </div>
);



export default function Nosotros() {
  return (
    <div className="nosotros-page">
      {/* HERO */}
      <section className="hero-section">
        <div className="container">
          <p className="hero-subtitle">
            SOBRE MYM DENTAL
          </p>
          <h1 className="hero-title">
            Innovando la forma de adquirir
            <br />
            insumos dentales.
          </h1>
          <p className="hero-text">
            En MyM Dental creemos que la tecnología puede transformar la forma en
            que clínicas y profesionales de la odontología adquieren sus
            materiales. Nuestra plataforma ofrece una experiencia simple,
            moderna y segura para acceder a un amplio catálogo de productos
            especializados.
          </p>
        </div>
      </section>
      {/* HISTORIA */}
      <section className="history-section">
        <div className="container">
          <div className="history-card">
            <h2>Nuestra Historia</h2>
            <p>
              MyM Dental nació con el objetivo de modernizar la compra de
              insumos odontológicos. Nuestro compromiso es facilitar el acceso
              a productos especializados mediante una plataforma intuitiva,
              transparente y enfocada en las necesidades de clínicas,
              profesionales y estudiantes del área dental.
            </p>
          </div>
        </div>
      </section>
      {/* MISIÓN Y VISIÓN */}
      <section className="mission-section">
        <div className="container">
          <div className="row g-4">
            <div className="col-lg-6">
              <div className="info-card">
                <h3>Nuestra Misión</h3>
                <p>
                  Proporcionar una plataforma confiable para la adquisición de
                  insumos odontológicos, entregando una experiencia moderna,
                  sencilla y enfocada en la calidad de nuestros productos.
                </p>
              </div>
            </div>
            <div className="col-lg-6">
              <div className="info-card">
                <h3>Nuestra Visión</h3>
                <p>
                  Ser una empresa referente en el comercio electrónico de
                  insumos dentales, destacando por la innovación, confianza y
                  compromiso con el sector odontológico.
                </p>
              </div>
            </div>
          </div>
       </div>
     </section>
     {/* POR QUÉ ELEGIRNOS */}
      <section className="values-section">
        <div className="container">
          <h2 className="section-title">
            ¿Por qué elegir MyM Dental?
          </h2>
          <div className="row">
            <FeatureCard
              title="Especialización"
              description="Nuestro catálogo está enfocado exclusivamente en productos para el área odontológica, facilitando la búsqueda de los insumos adecuados."
            />
            <FeatureCard
              title="Transparencia"
              description="Brindamos información clara y actualizada sobre cada producto para que puedas tomar decisiones con confianza."
            />
            <FeatureCard
              title="Innovación"
              description="Apostamos por una plataforma moderna y eficiente que simplifique la experiencia de compra para nuestros clientes."
            />
          </div>
        </div>
      </section>
    </div>
  );
}