<template>
  <div class="pa-6">
    <h2>📚 도서 열람</h2>

    <!-- ✅ 검색 입력창 추가 -->
    <v-text-field
      v-model="search"
      label="도서 제목 검색"
      append-icon="mdi-magnify"
      clearable
      outlined
      class="mt-4"
    />

    <!-- 🔁 필터링된 도서가 없을 경우 메시지 표시 -->
    <div v-if="filteredBooks.length === 0">
      <p class="text-grey">검색된 도서가 없습니다.</p>
    </div>

    <!-- ✅ 검색된 도서 목록을 v-for로 출력 -->
    <div v-else>
      <v-card
        v-for="(book, index) in filteredBooks"
        :key="index"
        class="mt-4"
      >
        <v-card-title>{{ book.title }}</v-card-title>
        <v-card-text>{{ book.content }}</v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const books = ref([])         // 전체 도서 목록
const search = ref('')        // 검색어 입력 상태

// ✅ 검색된 도서만 반환하는 계산 속성
const filteredBooks = computed(() =>
  books.value.filter(book =>
    book.title.toLowerCase().includes(search.value.toLowerCase())
  )
)

onMounted(() => {
  books.value = JSON.parse(localStorage.getItem('writtenPosts') || '[]')
})
</script>