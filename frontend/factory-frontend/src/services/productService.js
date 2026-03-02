import axios from 'axios'

const API_URL = 'http://localhost:8080/products'

export const getProducts = () => {
  return axios.get(API_URL)
}

export const createProduct = (data) => {
  return axios.post(API_URL, data)
}

export const updateProduct = (id, data) => {
  return axios.put(`${API_URL}/${id}`, data)
}

export const deleteProduct = (id) => {
  return axios.delete(`${API_URL}/${id}`)
}

export const filterProductsByMaterial = (material) => {
  return axios.get(`${API_URL}/filter`, {
    params: { material }
  })
}
