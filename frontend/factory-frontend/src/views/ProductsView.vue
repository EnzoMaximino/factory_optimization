<script setup>
import { ref, onMounted } from 'vue'
import {
  getProducts,
  createProduct,
  updateProduct,
  deleteProduct
} from '../services/productService'
import { getMaterials } from '../services/materialService'

/* =========================
   STATE
========================= */

const products = ref([])
const materials = ref([])
const loading = ref(false)

const showModal = ref(false)
const isEditing = ref(false)
const editingId = ref(null)

const form = ref({
  name: '',
  value: 0,
  composition: []
})

/* =========================
   FETCH
========================= */

const fetchProducts = async () => {
  loading.value = true
  try {
    const response = await getProducts()
    products.value = response.data.content
  } catch (error) {
    console.error('Erro ao buscar produtos:', error)
  } finally {
    loading.value = false
  }
}

const fetchMaterials = async () => {
  try {
    const response = await getMaterials()
    materials.value = response.data   // 🔥 corrigido (sem .content)
  } catch (error) {
    console.error('Erro ao buscar materiais:', error)
  }
}

/* =========================
   COMPOSITION CONTROL
========================= */

const addMaterialRow = () => {
  form.value.composition.push({
    materialId: null,
    quantity: 0
  })
}

const removeMaterialRow = (index) => {
  form.value.composition.splice(index, 1)
}

/* =========================
   MODAL CONTROL
========================= */

const openCreateModal = async () => {
  await fetchMaterials()

  isEditing.value = false
  editingId.value = null

  form.value = {
    name: '',
    value: 0,
    composition: []
  }

  addMaterialRow()
  showModal.value = true
}

const openEditModal = async (product) => {
  await fetchMaterials()

  isEditing.value = true
  editingId.value = product.id

  form.value = {
    name: product.name,
    value: product.value,
    composition: product.composition.map(item => ({
      materialId: item.materialId,
      quantity: item.quantity
    }))
  }

  showModal.value = true
}

/* =========================
   SAVE
========================= */

const saveProduct = async () => {
  if (!form.value.name.trim()) {
    alert("Name is required.")
    return
  }

  if (form.value.value <= 0) {
    alert("Value must be greater than zero.")
    return
  }

  if (form.value.composition.length === 0) {
    alert("Product must have at least one material.")
    return
  }

  for (const item of form.value.composition) {
    if (!item.materialId || item.quantity <= 0) {
      alert("Invalid material composition.")
      return
    }
  }

  try {
    if (isEditing.value) {
      await updateProduct(editingId.value, form.value)
    } else {
      await createProduct(form.value)
    }

    showModal.value = false
    fetchProducts()

  } catch (error) {
    console.error("Erro ao salvar produto:", error)
  }
}

/* =========================
   DELETE
========================= */

const removeProduct = async (id) => {
  if (!confirm("Delete this product?")) return

  try {
    await deleteProduct(id)
    fetchProducts()
  } catch (error) {
    console.error("Erro ao deletar produto:", error)
  }
}

onMounted(fetchProducts)
</script>

<template>
  <div class="card">

    <!-- HEADER -->
    <div class="products-header">
      <h2 class="page-title">Products</h2>
      <button class="primary-btn" @click="openCreateModal">
        + New Product
      </button>
    </div>

    <!-- TABLE -->
    <table class="products-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Value</th>
          <th>Materials(g)</th>
          <th>Actions</th>
        </tr>
      </thead>

      <tbody>

        <tr v-if="products.length === 0">
          <td colspan="4" class="empty-state">
            No products registered yet.
          </td>
        </tr>

        <tr v-for="product in products" :key="product.id">
          <td>{{ product.name }}</td>
          <td>R$ {{ Number(product.value).toFixed(2) }}</td>
          <td>
            <div v-for="item in product.composition" :key="item.materialId">
              {{ item.materialName }} ({{ item.quantity }})
            </div>
          </td>
          <td>
            <button class="edit-btn" @click="openEditModal(product)">
              Edit
            </button>
            <button class="delete-btn" @click="removeProduct(product.id)">
              Delete
            </button>
          </td>
        </tr>

      </tbody>
    </table>

  </div>

  <!-- MODAL -->
  <div v-if="showModal" class="modal-overlay">
    <div class="modal">

      <h3>
        {{ isEditing ? 'Edit Product' : 'New Product' }}
      </h3>

      <div class="form-group">
        <label class="form-label">Product Name: </label>
        <input
          v-model="form.name"
          placeholder="Enter product name"
          class="input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">Unit Price (R$)</label>
        <input
          v-model.number="form.value"
          type="number"
          placeholder="Enter product price"
          class="input"
        />
      </div>

      <h4>Composition (Materials required per unit)</h4>

      <div
        v-for="(item, index) in form.composition"
        :key="index"
        class="composition-row"
      >

        <div class="composition-field">
          <label class="form-label-small">Material</label>
          <select v-model.number="item.materialId" class="input">
            <option disabled :value="null">Select Material</option>
            <option
              v-for="mat in materials"
              :key="mat.id"
              :value="mat.id"
            >
              {{ mat.name }}
            </option>
          </select>
        </div>

        <div class="composition-field">
          <label class="form-label-small">Quantity Used</label>
          <input
            v-model.number="item.quantity"
            type="number"
            placeholder="Quantity"
            class="input"
          />
        </div>

        <button
          @click="removeMaterialRow(index)"
          class="delete-btn"
          style="margin-top:22px"
        >
          X
        </button>

      </div>

      <button
        @click="addMaterialRow"
        class="edit-btn"
        style="margin-top:10px"
      >
        + Add Material
      </button>

      <div class="modal-actions">
        <button
          class="primary-btn"
          @click="saveProduct"
        >
          {{ isEditing ? 'Update' : 'Save' }}
        </button>

        <button
          class="cancel-btn"
          @click="showModal = false"
        >
          Cancel
        </button>
      </div>

    </div>
  </div>
</template>

<style scoped>

.products-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.products-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.products-table thead {
  background: #f1f5f9;
}

.products-table th {
  padding: 16px;
  text-align: left;
  font-weight: 600;
  color: #334155;
}

.products-table td {
  padding: 14px 16px;
}

.products-table tbody tr {
  border-top: 1px solid #e2e8f0;
}

.products-table tbody tr:hover {
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

.primary-btn:hover {
  background-color: #1d4ed8;
}

.edit-btn {
  background: #eab308;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  margin-right: 6px;
  cursor: pointer;
}

.delete-btn {
  background: #ef4444;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
}

.cancel-btn {
  background: #e2e8f0;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;
  padding: 30px;
  border-radius: 12px;
  width: 400px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.input {
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
}

.composition-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

</style>
