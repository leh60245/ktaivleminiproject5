<template>
  <div class="pa-8" style="background-color: #f5f5f5; min-height: 100vh;">
    <h2 class="mb-8">🤖 AI 미리보기</h2>

    <v-text-field
      v-model="title"
      label="제목"
      outlined
      dense
      class="mb-6"
      style="background-color: white;"
    />

    <v-textarea
      v-model="content"
      label="본문 내용"
      rows="8"
      auto-grow
      outlined
      dense
      class="mb-6"
      style="background-color: white;"
    />

    <v-btn color="primary" class="mb-8" @click="generatePreview">
      미리보기 생성
    </v-btn>

    <v-divider class="my-6" />

    <div v-if="cover || summary">
      <v-img
        :src="cover"
        height="200px"
        class="mb-6"
        style="border: 1px solid #ddd;"
      />

      <v-card style="background-color: white;" class="pa-4">
        <v-card-title>📄 AI 요약</v-card-title>
        <v-card-text>{{ summary }}</v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const title = ref('')
const content = ref('')
const cover = ref('')
const summary = ref('')

function generatePreview() {
  if (!title.value || !content.value) {
    alert("제목과 본문 내용을 입력해주세요.")
    return
  }

  // 시뮬레이션용 placeholder
  cover.value = `https://placehold.co/300x200?text=${encodeURIComponent(title.value)}`
  summary.value = content.value.slice(0, 100) + '...'
}
</script>
