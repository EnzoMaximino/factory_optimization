import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '../layouts/AppLayout.vue'

import HomeView from '../views/HomeView.vue'
import MaterialsView from '../views/MaterialsView.vue'
import ProductsView from '../views/ProductsView.vue'
import ProductionView from '../views/ProductionView.vue'

const routes = [
  {
    path: '/',
    component: AppLayout,
    children: [
      { path: '', component: HomeView },
      { path: 'materials', component: MaterialsView },
      { path: 'products', component: ProductsView },
      { path: 'production', component: ProductionView }
    ]
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
