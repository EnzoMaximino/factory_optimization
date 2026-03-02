<script setup>
import { ref, onMounted } from 'vue'
import {
  getMaterials,
  createMaterial,
  updateMaterial,
  deleteMaterial
} from '../services/materialService'

/* =========================
   STATE
========================= */

const materials = ref([])
const loading = ref(false)

const showModal = ref(false)
const isEditing = ref(false)
const editingId = ref(null)

const form = ref({
  name: '',
  quantity: 0,
  unit: 'GRAM'
})

/* =========================
   FETCH
========================= */

const fetchMaterials = async () => {
  try {
    loading.value = true
    const response = await getMaterials()
    materials.value = response.data
  } catch (error) {
    console.error('Erro ao buscar materiais:', error)
  } finally {
    loading.value = false
  }
}

/* =========================
   MODAL CONTROL
========================= */

const openCreateModal = () => {
  isEditing.value = false
  editingId.value = null
  form.value = { name: '', quantity: 0, unit: 'GRAM' }
  showModal.value = true
}

const openEditModal = (material) => {
  isEditing.value = true
  editingId.value = material.id
  form.value = { ...material }
  showModal.value = true
}

/* =========================
   SAVE
========================= */

const saveMaterial = async () => {
  if (!form.value.name || form.value.name.trim() === '') {
    alert('Name is required.')
    return
  }

  if (!form.value.quantity || form.value.quantity <= 0) {
    alert('Quantity must be greater than zero.')
    return
  }

  try {
    if (isEditing.value) {
      await updateMaterial(editingId.value, form.value)
    } else {
      await createMaterial(form.value)
    }

    showModal.value = false
    fetchMaterials()
  } catch (error) {
    console.error('Erro ao salvar material:', error)
  }
}


/* =========================
   DELETE
========================= */

const removeMaterial = async (id) => {
  const confirmed = confirm('Are you sure you want to delete this material?')
  if (!confirmed) return

  try {
    await deleteMaterial(id)
    fetchMaterials()
  } catch (error) {
    console.error('Erro ao deletar material:', error)
  }
}

onMounted(fetchMaterials)
</script>

<template>
  <div class="card">

    <!-- HEADER -->
    <div class="materials-header">
      <h2 class="page-title">Materials</h2>
      <button class="primary-btn" @click="openCreateModal">
        + New Material
      </button>
    </div>

    <!-- LOADING -->
    <div v-if="loading" class="loading">
      Loading materials...
    </div>

    <!-- TABLE -->
    <table v-else class="materials-table">

      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Quantity</th>
          <th>Actions</th>
        </tr>
      </thead>

      <tbody>

        <tr v-if="materials.length === 0">
          <td colspan="4" class="empty-state">
            No materials registered yet.
          </td>
        </tr>

        <tr v-for="material in materials" :key="material.id">
          <td>{{ material.id }}</td>
          <td>{{ material.name }}</td>
          <td>
            {{ material.quantity }}
            <span class="unit-badge">
              {{ material.unit }}
            </span>
          </td>
          <td>
            <button class="edit-btn" @click="openEditModal(material)">
              Edit
            </button>
            <button class="delete-btn" @click="removeMaterial(material.id)">
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
        {{ isEditing ? 'Edit Material' : 'New Material' }}
      </h3>

      <div class="form-group">
        <label class="form-label">Material Name: </label>
        <input
          v-model="form.name"
          placeholder="Enter material name"
          class="input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">Quantity: </label>
        <input
          v-model.number="form.quantity"
          type="number"
          min="0.01"
          step="0.01"
          placeholder="Enter available quantity"
          class="input"
        />
      </div>

      <div class="form-group">
        <label class="form-label">Unit of Measure: </label>
        <select v-model="form.unit" class="input">
          <option value="GRAM">G</option>
          <option value="KILOGRAM">KG</option>
          <option value="LITER">L</option>
          <option value="MILLILITER">ML</option>
        </select>
      </div>

      <div class="modal-actions">
        <button class="primary-btn" @click="saveMaterial">
          {{ isEditing ? 'Update' : 'Save' }}
        </button>

        <button class="cancel-btn" @click="showModal = false">
          Cancel
        </button>
      </div>

    </div>
  </div>
</template>

<style scoped>

/* HEADER */
.materials-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

/* TABLE */
.materials-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.materials-table thead {
  background: #f1f5f9;
}

.materials-table th {
  padding: 16px;
  text-align: left;
  font-weight: 600;
  color: #334155;
}

.materials-table td {
  padding: 14px 16px;
}

.materials-table tbody tr {
  border-top: 1px solid #e2e8f0;
}

.materials-table tbody tr:hover {
  background: #f8fafc;
}

/* EMPTY */
.empty-state {
  text-align: center;
  padding: 30px;
  color: #64748b;
}

/* BADGE */
.unit-badge {
  background: #2563eb;
  color: white;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 20px;
  margin-left: 8px;
}

/* BUTTONS */
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

/* MODAL */
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
  width: 350px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.input {
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.loading {
  padding: 20px;
  font-weight: 500;
}

</style>
