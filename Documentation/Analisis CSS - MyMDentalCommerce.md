# Análisis de CSS / Frontend — MyMDentalCommerce

## 1. Estructura de Archivos CSS

- 16 archivos CSS en `src/Styles/` (uno por componente/página)
- 2 archivos globales: `index.css` y `App.css`
- Sin preprocesadores — CSS plano
- Sin frameworks utilitarios (no Tailwind, no PostCSS)
- Framework visual: Bootstrap 5

---

## 2. Gama de Colores

### Paleta Principal (MYM)

| Color | Hex | Uso |
|-------|-----|-----|
| Cian | `#00C2CB` | Color de marca — navbar, botones, enlaces, bordes hover |
| Cian oscuro | `#009BA3` / `#00a8b0` | Hover de botones |
| Rosa intenso | `#FF007F` | Precios, errores, acento secundario |
| Gris claro | `#F8F9FA` | Fondos de página y tarjetas |
| Blanco | `#FFFFFF` | Superficies, tarjetas, formularios |
| Gris oscuro | `#2D3436` | Texto principal y títulos |
| Gris medio | `#636E72` | Texto secundario / muted |
| Gris claro | `#b2bec3` | Copyright, textos deshabilitados |

### Inconsistencias detectadas

| Archivo | Problema |
|---------|----------|
| `Administrador.css` | Usa azul `#2563eb` en vez de cian `#00C2CB` |
| `Carrito.css` | Usa azul Bootstrap `#007bff`, verde `#28a745`, rojo `#dc3545` |
| `Inicio_sesion.css` / `Crear_cuenta.css` | Texto de input `#ff00ea` (magenta neón ilegible); botón con gradiente azul ajeno a la paleta |
| `Loader.css` | Spinner azul `#3498db` en vez de cian |
| `index.css` | Variables dark mode con púrpura `#aa3bff` no usadas en ningún componente |

---

## 3. Tipografía

| Elemento | Fuente | Tamaño | Peso |
|----------|--------|--------|------|
| Body | `system-ui, 'Segoe UI', Roboto, sans-serif` | 18px (→16px en <1024px) | normal |
| h1 | Misma sans-serif | 56px | 500 |
| h2 | Misma sans-serif | 24px | 500 |
| Títulos sección | Sans-serif | 26px | 700 |
| Precios | Sans-serif | 1rem | 700 |
| Botones | Sans-serif | 14–16px | 600–700 |
| Código | `ui-monospace, Consolas, monospace` | 15px | normal |

**Inconsistencia**: `.encabezadoHome` usa `'Times New Roman', Times, serif` — rompe con el sistema sans-serif del resto del sitio.

---

## 4. Radios de Borde

| Elemento | Border-radius |
|----------|--------------|
| Tarjetas de producto | 12px |
| Contenedor Login/Register | 36px |
| Formularios antiguos | 15px |
| Inputs y botones | 8px |
| Botón "Detalles" | 20px (pill) |
| Tarjeta de Perfil | 16px |

---

## 5. Sombras

- **Tarjetas**: `0 2px 8px rgba(0,194,203,0.08)` → hover: `0 8px 24px rgba(0,194,203,0.18)`
- **Auth container**: `0 8px 30px rgba(0,0,0,0.12)`
- **Formularios antiguos**: `0 10px 30px rgba(0,0,0,0.5)`
- **Perfil**: Sistema de variables `--mym-shadow-sm/md/lg`

---

## 6. Animaciones Destacadas

- Underline animado en nav-links (0→100% width en hover)
- Elevación de tarjetas (`translateY(-4px)`) + sombra
- Shimmer en botones (gradiente deslizante)
- Logo navbar: `scale(1.08) rotate(-2deg)` en hover
- Transiciones login/register con slide + opacidad
- Iconos redes con `translateY(-3px)` + fondo cian

---

## 7. Responsive Design

| Breakpoint | Afecta |
|-----------|--------|
| 1024px | Font-size global, layout hero |
| 768px | Carrito → 1 columna |
| 640px | Perfil/EditarDatos → apilamiento |
| 600px | Orders → columna |
| 480px | Login-Register → 1 columna |

---

## 8. Problemas y Recomendaciones

1. **Migrar a variables CSS**: Solo Perfil.css define `--mym-*`. Unificar todo en `index.css`.
2. **Admin fuera de paleta**: Cambiar azul `#2563eb` → cian `#00C2CB`.
3. **Carrito con colores default**: `#007bff` → `#00C2CB`, `#dc3545` → `#FF007F`, `#28a745` → `#00C2CB`.
4. **Formularios duplicados**: `Inicio_sesion.css` y `Crear_cuenta.css` casi idénticos, con texto magenta ilegible.
5. **`Times New Roman` en home**: Unificar a sans-serif.
6. **Loader azul**: `#3498db` → `#00C2CB`.
7. **`AdminProduct.css` vacío**: Eliminar o implementar.
8. **Dark mode sin uso**: Variables de `index.css` no aplicadas en componentes.
9. **Gradientes inconsistentes**: Login-Register moderno usa sólidos; los formularios antiguos usan gradientes distintos.
