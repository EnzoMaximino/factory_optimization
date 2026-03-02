<template>
  <div class="app-container">

    <aside class="sidebar">
      <h1>Factory OS</h1>

      <router-link to="/" class="nav-link">Home</router-link>
      <router-link to="/materials" class="nav-link">Materials</router-link>
      <router-link to="/products" class="nav-link">Products</router-link>
      <router-link to="/production" class="nav-link">Production</router-link>
    </aside>

    <main class="main-content">
      <router-view />
    </main>

  </div>
</template>
