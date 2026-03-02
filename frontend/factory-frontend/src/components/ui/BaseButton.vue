<script setup>
defineProps({
  variant: {
    type: String,
    default: 'primary'
  }
})
</script>

<template>
  <button
    :class="[
      'px-4 py-2 rounded-lg font-medium transition',
      variant === 'primary' && 'bg-blue-600 text-white hover:bg-blue-700',
      variant === 'danger' && 'bg-red-600 text-white hover:bg-red-700',
      variant === 'secondary' && 'bg-gray-200 hover:bg-gray-300'
    ]"
  >
    <slot />
  </button>
</template>
