<template>
  <div class="pa-6">
    <h2>📚 도서 열람</h2>

    <v-select
      v-model="selected"
      :items="books"
      label="도서 선택"
      item-title="title"
      return-object
      outlined
    />

    <v-card v-if="selected" class="mt-4">
      <v-card-title>{{ selected.title }}</v-card-title>
      <v-card-text>{{ selected.content }}</v-card-text>
    </v-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const books = ref([])
const selected = ref(null)

onMounted(() => {
  books.value = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
})
</script>
