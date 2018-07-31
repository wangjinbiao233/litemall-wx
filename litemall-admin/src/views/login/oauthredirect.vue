<template>
<el-icon-loading></el-icon-loading>
</template>

<script>
    export default {
        name: "oauthredirect",
        created: function () {
            let token = this.$route.query.token
            this.$store.dispatch('LoginByOAuth', token).then(() => {
              this.$router.push({ path: '/' })
              }).catch(() => {
            })
        }
    }
</script>

<style scoped>

</style>
