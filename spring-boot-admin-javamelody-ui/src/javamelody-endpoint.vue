<template>
  <div class="iframe-box" style="">
    <iframe :src="src"/>
  </div>
</template>

<script>
export default {
  data () {
    return {
      src: '/javamelody?application=' + this.$route.params.instanceId
    }
  }
}
</script>

<style>
.iframe-box{
  background-color: #ffffff;
  width: 100%;
  height: calc(100vh - 56px);
  padding-right: 0px;
  padding-left: 0px;
}

.iframe-box iframe{
    width: 100%;
    height: 100%;
}
</style>