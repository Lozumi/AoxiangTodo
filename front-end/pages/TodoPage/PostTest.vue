<template>
  <div>
    <h1>Data Fetching Example</h1>

    <div v-if="result">
      <h2>Status:</h2>
      <p>Status: {{ result.status }}</p>
      <p>Message: {{ result.message }}</p>
      <p>Content: {{ result.content }}</p>
    </div>

    <div v-if="error">
      <h2>Error:</h2>
      <pre>{{ error }}</pre>
    </div>

    <div v-if="pending">Loading...</div>
  </div>
</template>

<script setup lang="ts">

const apiUrl = 'http://localhost:20220/'; // Replace with your API endpoint
const { data, pending, error, refresh } = useFetch(apiUrl, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({ /* your request body */ }),
  transformResponse: (text) => {
    // Parse the JSON string returned from the API
    try {
      return JSON.parse(text);
    } catch (error) {
      console.error('Error parsing response:', error);
      return null;
    }
  },
});

const result = ref(null);

// Update the result when data changes
onMounted(() => {
  refresh();
});

// Watch for changes in the fetched data
watch(data, (newData) => {
  result.value = newData;
});
</script>
