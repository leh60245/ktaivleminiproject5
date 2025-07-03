<script setup>
import { ref, computed, onMounted } from 'vue'

const books = ref([])

function loadPosts() {
  books.value = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
}

function deletePost(id) {
  if (!confirm('정말 삭제하시겠습니까?')) return
  const updated = books.value.filter(post => post.id !== id)
  localStorage.setItem('writtenPosts', JSON.stringify(updated))
  books.value = updated
}

function publishPost(id) {
  const existing = JSON.parse(localStorage.getItem('writtenPosts') || '[]')

  const updated = existing.map(post => {
    if (post.id === id) {
      return {
        ...post,
        isPublished: true,
        summary: post.summary || 'AI가 생성한 요약입니다.',
        cover: post.cover || 'https://via.placeholder.com/300x200?text=AI+표지'
      }
    }
    return post
  })

  localStorage.setItem('writtenPosts', JSON.stringify(updated))
  books.value = updated

  alert('출간 요청 완료! AI 표지와 요약이 생성됩니다.')
}

const unpublishedBooks = computed(() => books.value.filter(book => !book.isPublished))
const publishedBooks = computed(() => books.value.filter(book => book.isPublished))

onMounted(() => {
  loadPosts()
})
</script>

<template>
  <div class="pa-6">
    <h2>📚 작가 대시보드</h2>

    <v-btn color="primary" @click="$router.push('/writer/write')">
      ✍ 글 작성하러 가기
    </v-btn>

    <v-divider class="my-5" />

    <div v-if="books.length">
      <!-- 출간 전 글 목록 -->
      <h3>출간 준비 중</h3>
      <v-row>
        <v-col
          v-for="book in unpublishedBooks"
          :key="book.id"
          cols="12"
          sm="6"
          md="4"
        >
          <v-card>
            <v-img
              height="200"
              :src="book.cover || 'https://via.placeholder.com/300x200?text=미리보기+없음'"
              cover
            />
            <v-card-title>{{ book.title }}</v-card-title>
            <v-card-subtitle>{{ book.date }}</v-card-subtitle>
            <v-card-actions>
              <v-btn color="primary" @click="publishPost(book.id)">
                출간 요청
              </v-btn>
              <v-btn color="warning" @click="$router.push({ path: '/writer/edit', query: { id: book.id } })">
                수정
              </v-btn>
              <v-btn color="error" @click="deletePost(book.id)">
                삭제
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>

      <v-divider class="my-5" />

      <!-- 출간된 글 목록 -->
      <h3>출간 완료</h3>
      <v-row>
        <v-col
          v-for="book in publishedBooks"
          :key="book.id"
          cols="12"
          sm="6"
          md="4"
        >
          <v-card>
            <v-img
              height="200"
              :src="book.cover || 'https://via.placeholder.com/300x200?text=AI+표지'"
              cover
            />
            <v-card-title>{{ book.title }}</v-card-title>
            <v-card-subtitle>{{ book.date }}</v-card-subtitle>
            <v-card-actions>
              <v-btn disabled color="success">출간됨</v-btn>
              <v-btn color="warning" @click="$router.push({ path: '/writer/edit', query: { id: book.id } })">
                수정
              </v-btn>
              <v-btn color="error" @click="deletePost(book.id)">
                삭제
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </div>

    <p v-else>작성한 글이 아직 없습니다.</p>
  </div>
</template>