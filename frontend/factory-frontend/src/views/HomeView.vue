<script setup>
import { ref, onMounted } from 'vue'
import { getMaterials } from '../services/materialService'
import { getProducts } from '../services/productService'

const totalMaterials = ref(0)
const totalProducts = ref(0)

const fetchDashboardData = async () => {
  try {
    const materialsResponse = await getMaterials()
    const productsResponse = await getProducts()

    totalMaterials.value = materialsResponse.data.length
    totalProducts.value = productsResponse.data.totalElements

  } catch (error) {
    console.error('Erro ao carregar dashboard:', error)
  }
}

onMounted(fetchDashboardData)
</script>

<template>
  <div class="card">

    <h2 class="page-title">Dashboard</h2>

    <div class="stats-container">

      <div class="stat-box">
        <h3>Total Materials</h3>
        <p>{{ totalMaterials }}</p>
      </div>

      <div class="stat-box">
        <h3>Total Products</h3>
        <p>{{ totalProducts }}</p>
      </div>

    </div>

  </div>
</template>

<style scoped>
.stats-container {
  display: flex;
  gap: 20px;
}

.stat-box {
  flex: 1;
  background: #f8fafc;
  padding: 24px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.stat-box h3 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #64748b;
}

.stat-box p {
  font-size: 26px;
  font-weight: 600;
}
</style>
