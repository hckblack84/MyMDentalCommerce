import { useState, useEffect } from "react";

//Solo era para ver si funcionaba el hook, pero ya funciona xd
//import { getClientProducts } from "../Service/ProductsService";
import { getClientProductsByPage, getClientProductsByPageFilter } from "../Service/GetProductsService";
import { getDatesByProducts, getDatesByProductsFilter } from "../Service/GetProductsService";


export function useProductsState(indexPage = 1, isFiltered = false, filter = "") {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [errorBody, setErrorBody] = useState(null);

  const [maxPages, setMaxPages] = useState(1);
  const [currentPage, setCurrentPage] = useState(indexPage);
  const [loadingPages, setLoadingPages] = useState(true);
  const [errorPages, setErrorPages] = useState(false);
  const [errorBodyPages, setErrorBodyPages] = useState(null);

  const fetchClientProducts = (page, filterValue) => {
    const pageToFetch = page ?? 1;
    const searchFilter = filterValue ?? "";

    setLoading(true);
    setError(false);
    setErrorBody(null);
    setProducts([]);

    const globalProductRequest = isFiltered
      ? getClientProductsByPageFilter(searchFilter, pageToFetch)
      : getClientProductsByPage(pageToFetch);

    globalProductRequest
      .then((result) => {setProducts(result); console.log(result)})
      .catch((err) => {
        setError(true);
        setErrorBody(err.body ?? {
          code: err.code ?? err.status,
          message: err.message,
        });
        console.error(err);
      })
      .finally(() => setLoading(false));
  };

  const fetchDates = (page, filterValue) => {
    const pageToFetch = page ?? 1;
    const searchFilter = filterValue ?? "";

    setLoadingPages(true);
    setErrorPages(false);
    setErrorBodyPages(null);

    const globalDatesRequest = isFiltered
      ? getDatesByProductsFilter(searchFilter, pageToFetch)
      : getDatesByProducts(pageToFetch);

    globalDatesRequest
      .then((data) => {
        setMaxPages(data.totalPages);
        setCurrentPage(pageToFetch);
      })
      .catch((err) => {
        setErrorPages(true);
        setErrorBodyPages(err.body ?? {
          code: err.code,
          message: err.message,
        });
        console.error(err);
      })
      .finally(() => setLoadingPages(false));
  };

  const reloadProducts = () => {
    fetchClientProducts(currentPage, filter);
    fetchDates(currentPage, filter);
  };

  const searchProductsByPage = (page) => {
    const pageToFetch = page ?? 1;
    setCurrentPage(pageToFetch);
    fetchClientProducts(pageToFetch, filter);
    fetchDates(pageToFetch, filter);
  };

  useEffect(() => {
    searchProductsByPage(indexPage);
  }, [indexPage, isFiltered, filter]);

  const productsState = { products, loading, error, errorBody, searchProductsByPage, reloadProducts };
  const pagesState = {
    maxPages,
    currentPage,
    loadingPages,
    errorPages,
    errorBodyPages,
    getDatesByProductsPage: fetchDates,
  };

  return { productsState, pagesState };
}
