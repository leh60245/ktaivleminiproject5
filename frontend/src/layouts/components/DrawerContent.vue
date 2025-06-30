<script setup>
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'
</script>

<template>
    <ul>
        <VerticalNavSectionTitle :item="{ heading: '작가 관리' }" />
        <VerticalNavLink
            :item="{
                title: '작가',
                to: '/authors',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '작가 통계 조회',
                to: '/authorStatistics',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '구독 및 열람 관리' }" />
        <VerticalNavLink
            :item="{
                title: '구독',
                to: '/subscriptions',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '서재(도서 목록) 관리' }" />
        <VerticalNavLink
            :item="{
                title: '도서',
                to: '/books',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '회원 및 포인트 관리' }" />
        <VerticalNavLink
            :item="{
                title: '회원',
                to: '/users',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '포인트 소진 상태',
                to: '/pointExhaustedStatuses',
            }"
        />
        <VerticalNavSectionTitle :item="{ heading: '원고 및 출간 관리' }" />
        <VerticalNavLink
            :item="{
                title: '원고',
                to: '/manuscripts',
            }"
        />
        <VerticalNavLink
            :item="{
                title: '출간 요청',
                to: '/publicationRequests',
            }"
        />
    </ul>
</template>
