<template>
	<div class="app-wrapper">
		<div>
			<padNavbar></padNavbar>
			<app-main></app-main>
		</div>
	</div>
</template>

<script>
import { PadNavbar, AppMain } from './components'
import { mapGetters } from 'vuex'

export default {
  name: 'layout',
  components: {
		AppMain,
		PadNavbar
  },
	created () {
    //this.initDatas()
  },
  computed: {
		...mapGetters([
      'sidebar',
	  	'userId',
      'name',
      'avatar',
      'roles',
      'isManagers',
      'storename',
      'storeId',
			'dateTimes'
    ])
  },
	methods: {
		
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	@import "src/styles/mixin.scss";
	.app-wrapper {
	  @include clearfix;
	  position: relative;
	  height: 100%;
	  width: 100%;
	}
	.app-main{
		background-color: #f8f8f8;
	}
</style>
