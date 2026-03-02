import axios from 'axios'

const API_URL = 'http://localhost:8080/production'

export const getProductionPlan = () => {
  return axios.get(`${API_URL}/plan`)
}

export const executeProductionPlan = () => {
  return axios.post(`${API_URL}/execute`)
}


