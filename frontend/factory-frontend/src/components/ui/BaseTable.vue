<script setup>
defineProps({
  items: Array
})
</script>

<template>
  <table class="w-full border-collapse">
    <thead>
      <tr class="bg-gray-100 text-left">
        <th class="p-3">ID</th>
        <th class="p-3">Name</th>
        <th class="p-3">Stock</th>
      </tr>
    </thead>

    <tbody>
      <tr
        v-for="item in items"
        :key="item.id"
        class="border-t hover:bg-gray-50"
      >
        <td class="p-3">{{ item.id }}</td>
        <td class="p-3">{{ item.name }}</td>
        <td class="p-3">{{ item.stock }}</td>
      </tr>
    </tbody>
  </table>
</template>
