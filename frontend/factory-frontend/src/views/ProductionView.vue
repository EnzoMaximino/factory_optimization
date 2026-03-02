<script setup>

const executePlan = async () => {
  if (!confirm("This will update stock permanently. Continue?"))
    return

  try {
    const response = await executeProductionPlan()
    productionPlan.value = response.data

    alert("Stock updated successfully!")

  } catch (error) {
    console.error("Error executing plan:", error)
  }
}

import { ref } from 'vue'
import { getProductionPlan, executeProductionPlan} from '../services/productionService'

const productionPlan = ref([])
const loading = ref(false)
const totalValue = ref(0)

const calculatePlan = async () => {
  loading.value = true
  try {
    const response = await getProductionPlan()
    productionPlan.value = response.data

    totalValue.value = productionPlan.value.reduce(
      (sum, item) => sum + item.totalValue,
      0
    )

  } catch (error) {
    console.error('Error calculating production plan:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="card">

    <div class="production-header">
      <h2 class="page-title">Production Plan</h2>

      <button
        class="primary-btn"
        @click="calculatePlan"
        :disabled="loading"
      >
        {{ loading ? 'Calculating...' : 'Calculate Best Plan' }}
      </button>
      <button
        class="primary-btn"
        @click="executePlan"
      >
        Execute Plan
      </button>
    </div>

    <table class="production-table">

      <thead>
        <tr>
          <th>Product</th>
          <th>Unit Value</th>
          <th>Units To Produce</th>
          <th>Total Value</th>
        </tr>
      </thead>

      <tbody>

        <tr v-if="productionPlan.length === 0">
          <td colspan="4" class="empty-state">
            No calculation performed yet.
          </td>
        </tr>

        <tr v-for="item in productionPlan" :key="item.productId">
          <td>{{ item.productName }}</td>
          <td>R$ {{ Number(item.unitValue).toFixed(2) }}</td>
          <td>{{ item.unitsProduced }}</td>
          <td>R$ {{ Number(item.totalValue).toFixed(2) }}</td>
        </tr>

      </tbody>

    </table>

    <div v-if="productionPlan.length > 0" class="total-container">
      <strong>
        Total Expected Revenue:
        R$ {{ Number(totalValue).toFixed(2) }}
      </strong>
    </div>

  </div>
</template>

<style scoped>
.production-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.production-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.production-table thead {
  background: #f1f5f9;
}

.production-table th {
  padding: 16px;
  text-align: left;
  font-weight: 600;
  color: #334155;
}

.production-table td {
  padding: 14px 16px;
}

.production-table tbody tr {
  border-top: 1px solid #e2e8f0;
}

.production-table tbody tr:hover {
  background: #f8fafc;
}

.empty-state {
  text-align: center;
  padding: 30px;
  color: #64748b;
}

.primary-btn {
  background-color: #2563eb;
  color: white;
  padding: 10px 18px;
  border-radius: 8px;
  border: none;
  font-weight: 500;
  cursor: pointer;
}

.primary-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.primary-btn:hover:not(:disabled) {
  background-color: #1d4ed8;
}

.total-container {
  margin-top: 20px;
  text-align: right;
  font-size: 18px;
  color: #0f172a;
}
</style>
