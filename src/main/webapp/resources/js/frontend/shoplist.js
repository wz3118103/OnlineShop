$(function () {
    // 当前页面是否正在做加载？如果是，则不做加载
   var loading = false;
   var maxItems = 999;
   // 一页返回的最大条数
   var pageSize = 3;
   var listUrl = '/o2o/frontend/listshops';
   // 获取商店类别以及区域列表
   var searchDivUrl = '/o2o/frontend/listshopspageinfo';
   var pageNum = 1;
   var parentId = getQueryString('parentId');
   var areaId = '';
   var shopCategoryId = '';
   var shopName = '';
   // 渲染出商店类别以及区域列表
   getSearchDivData();
   // 预先加载10条店铺信息
    addItems(pageSize, pageNum);

    /**
     * 获取商店类别以及区域列表
     */
    function getSearchDivData() {
        var url = searchDivUrl + '?' + 'parentId=' + parentId;
        $.getJSON(url, function (data) {
            if (data.success) {
                var shopCategoryList = data.shopCategoryList;
                var html = '';
                html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
                shopCategoryList.map(function (item, index) {
                    html += '<a href="#" class="button" data-category-id=' + item.shopCategoryId + '>' +
                                item.shopCategoryName +
                            '</a>';
                });
                $('#shoplist-search-div').html(html);

                var selectOptions = '<option value="">全部街道</option>';
                var areaList = data.areaList;
                areaList.map(function (item, index) {
                    selectOptions += '<option value="' + item.areaId + '">' +
                                          item.areaName +
                                     '</option>';
                });
                $('#area-search').html(selectOptions);
            }
        });
    }

    /**
     * 分页展示商店列表
     * @param pageSize
     * @param pageIndex
     */
    function addItems(pageSize, pageIndex) {
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&parentId=' + parentId + '&areaId=' + areaId
            + '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
        loading = true;
        $.getJSON(url, function (data) {
           if(data.success) {
               // 获取总数，然后重置maxItems
               maxItems = data.count;
               var html = '';
               data.shopList.map(function (item, index) {
                   html +=  '<div class="card" data-shop-id="' + item.shopId + '">' +
                                '<div class="card-header">'  + item.shopName + '</div>' +
                                '<div class="card-content">' +
                                    '<div class="list-block media-list">' +
                                        '<ul>' +
                                            '<li class="item-content">' +
                                                '<div class="item-media">' +
                                                    '<img src="'+ item.shopImg + '" width="44">' +
                                                '</div>' +
                                                '<div class="item-inner">' +
                                                    '<div class="item-subtitle">' + item.shopDesc + '</div>' +
                                                '</div>' +
                                            '</li>' +
                                        '</ul>' +
                                    '</div>' +
                                '</div>' +
                                '<div class="card-footer">' +
                                    '<p class="color-gray">' +  new Date(item.lastEditTime).Format("yyyy-MM-dd") + '更新</p>' +
                                    '<span>点击查看</span>' +
                                '</div>' +
                            '</div>';
               });
               $('.list-div').append(html);
               var total = $('.list-div .card').length;
               // 达到最大的时候不能移除，否则不可重复操作
               if (total >= maxItems) {
                   // 隐藏提示符
                   $('.infinite-scroll-preloader').hide();
               } else {
                   $('.infinite-scroll-preloader').show();
               }
               pageNum += 1;
               loading = false;
               $.refreshScroller();
           }
        });
    }


    // 下滑屏幕自动进行分页搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function(){
        if (loading) {
            return;
        }
        addItems(pageSize, pageNum);
    });

    // 点击商店卡片进入该商店的详情页
    $('.shop-list').on('click', '.card', function (e) {
       var shopId = e.currentTarget.dataset.shopId;
       window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
    });

    // 选择新的商店类别之后，重置页码，清空原先的商店列表，按照新的类别去查询
    $('#shoplist-search-div').on('click', '.button', function (e) {
        if (parentId) {
            shopCategoryId = e.target.dataset.categoryId;
            // 若之前已选定了别的category,则移除其选定效果，改成选定新的
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                shopCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings()
                    .removeClass('button-fill');
            }
            // 由于查询条件改变，清空店铺列表再进行查询
            $('.list-div').empty();
            // 重置页码
            pageNum = 1;
            addItems(pageSize, pageNum);
        } else {
            parentId = e.target.dataset.categoryId;
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                parentId = '';
            } else {
                $(e.target).addClass('button-fill').siblings()
                    .removeClass('button-fill');
            }
            // 由于查询条件改变，清空店铺列表再进行查询
            $('.list-div').empty();
            // 重置页码
            pageNum = 1;
            addItems(pageSize, pageNum);
        }
    });

    // 需要查询的商店名字发生变化后，重置页码，清空原先的店铺列表，按照新的名字去查询
    // input太快了，改成change
    $('#search').on('change', function(e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    // 区域信息发生变化后，重置页码，清空原先的店铺列表，按照新的区域去查询
    $('#area-search').on('change', function() {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    // 点击后打开右侧栏
    $('#me').click(function() {
        $.openPanel('#panel-right-demo');
    });

    // 初始化页面
    $.init();

});