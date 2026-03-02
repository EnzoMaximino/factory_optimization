import axios from 'axios'

const API_URL = 'http://localhost:8080/materials'

export const getMaterials = () => {
  return axios.get(API_URL)
}

export const createMaterial = (data) => {
  return axios.post(API_URL, data)
}

export const updateMaterial = (id, data) => {
  return axios.put(`${API_URL}/${id}`, data)
}

export const deleteMaterial = (id) => {
  return axios.delete(`${API_URL}/${id}`)
}
