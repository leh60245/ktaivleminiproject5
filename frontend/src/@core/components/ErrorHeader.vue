<script setup>
const props = defineProps({
  errorCode: {
    type: String,
    required: true,
  },
  errorTitle: {
    type: String,
    required: true,
  },
  errorDescription: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <div class="text-center mb-4">
    <!-- 👉 Title and subtitle -->
    <h1 class="text-h1">
      
    </h1>
    <h5 class="text-h5 mb-1">
      
    </h5>
    <p class="text-sm">
      
    </p>
  </div>
</template>
