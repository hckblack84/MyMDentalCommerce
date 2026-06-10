import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

test("el enlace detalles apunta al producto correcto", () => {
  render(
    <MemoryRouter>
      <a href="/producto/1">Detalles</a>
    </MemoryRouter>
  );

  const detailsLink = screen.getByRole("link", {
    name: /detalles/i,
  });

  expect(detailsLink).toHaveAttribute(
    "href",
    "/producto/1"
  );
});