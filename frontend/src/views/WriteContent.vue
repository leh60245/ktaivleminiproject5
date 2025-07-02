<template>
  <div class="pa-8" style="background-color: #f5f5f5; min-height: 100vh;">
    <h2 class="mb-8">✍ 글 작성</h2>

    <v-text-field
      v-model="title"
      label="제목"
      outlined
      dense
      class="mb-6"
      :rules="[v => !!v || '제목은 필수입니다']"
      style="background-color: white;"
    />

    <v-select
      v-model="category"
      label="카테고리"
      :items="['소설', '시', '에세이']"
      outlined
      dense
      class="mb-6"
      style="background-color: white;"
    />

    <v-textarea
      v-model="summary"
      label="간단한 소개 (요약)"
      rows="2"
      auto-grow
      outlined
      dense
      class="mb-6"
      style="background-color: white;"
    />

    <v-combobox
      v-model="keywords"
      label="키워드 (AI용)"
      multiple
      chips
      outlined
      dense
      class="mb-6"
      hint="Enter 키로 키워드 추가"
      style="background-color: white;"
    />

    <v-textarea
      v-model="content"
      label="본문 내용"
      rows="12"
      auto-grow
      outlined
      dense
      class="mb-8"
      style="background-color: white;"
    />

    <div class="d-flex gap-4">
      <v-btn color="grey" @click="saveDraft">임시저장</v-btn>
      <v-btn color="primary" @click="submit">출간 요청</v-btn>
      <v-btn color="secondary" @click="previewAI">AI 미리보기</v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const title = ref('')
const category = ref('')
const summary = ref('')
const keywords = ref([])
const content = ref('')

function saveDraft() {
  alert('임시저장되었습니다 (추후 로컬/DB 저장 연동)')
}
function previewAI() {
  if (!title.value || !content.value) {
    alert("제목과 본문을 입력해주세요.")
    return
  }

  router.push({
    path: '/ai-preview',
    query: {
      title: title.value,
      content: content.value
    }
  })
}
function submit() {
  if (!title.value || !content.value) {
    alert("제목과 본문은 필수입니다.")
    return
  }

  const post = {
    id: Date.now(),
    title: title.value,
    category: category.value,
    summary: summary.value,
    keywords: keywords.value,
    content: content.value,
    date: new Date().toISOString().slice(0, 10)
  }

  const existing = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
  localStorage.setItem('writtenPosts', JSON.stringify([post, ...existing]))

  alert("출간 요청 완료!")
  router.push('/writer')
}
</script>