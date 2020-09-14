$(function () {
   var url = '/o2o/frontend/listmainpageinfo';
   $.getJSON(url, function (data) {
       if (data.success) {
           var headLineList = data.headLineList;
           var swiperHtml = '';
           headLineList.map(function (item, index) {
              swiperHtml +=  '<div class="swiper-slide img-wrap">' +
                                 '<a href="' + item.lineLink + '" external>' +
                                     '<img class="banner-img" src="' + item.lineImg  + '" alt="' + item.lineName + '">' +
                                 '</a>' +
                              '</div>';
           });
           $('.swiper-wrapper').html(swiperHtml);
           $('.swiper-container').swiper({
               autoplay:3000,
               // 用户对轮播图片进行操作时，是否自动停止autoplay
               autoplayDisableOnInteraction : false
           });

           var shopCategoryList = data.shopCategoryList;
           var categoryHtml = '';
           shopCategoryList.map(function (item, index) {
               categoryHtml += '<div class="col-50 shop-classify" data-category=' + item.shopCategoryId + '>' +
                                  '<div class="word">' +
                                      '<p class="shop-title">' + item.shopCategoryName  + '</p>' +
                                      '<p class="shop-desc">'  + item.shopCategoryDesc + '</p>' +
                                  '</div>' +
                                  ' <div class="shop-classify-img-warp">'  +
                                     '<img class="shop-img" src="' + item.shopCategoryImg + '">' +
                                  '</div>' +
                               '</div>';
           });
           $('.row').html(categoryHtml);
       }
   });

   // 点击工具栏“我的”，显式侧栏
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    // 点击一级商店类别时，附带上对应的一级类别的shopCategoryId
    $('.row').on('click', '.shop-classify', function (e) {
       var shopCategoryId = e.currentTarget.dataset.category;
       var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
       window.location.href = newUrl;
    });
});