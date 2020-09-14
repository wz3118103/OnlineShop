/**
 * 1.获取商店分类和区域列表信息
 * 2.点击提交的时候，将表单的内容全部获取到，通过ajax转发到后台
 * 3.还有一项待做的功能：必须要验证表单输入
 */
$(function () {
    var shopId = getQueryString('shopId');
    var isModify = shopId ? true : false;
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId;
    var modifyShopUrl = '/o2o/shopadmin/modifyshop';

    if (!isModify) {
        getShopInitInfo();
    } else {
        getShopInfo(shopId);
    }


    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                var tmpShopCategoryHtml = '';
                var tmpAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tmpShopCategoryHtml += '<option data-id="' + item.shopCategoryId
                        + '">' + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tmpAreaHtml += '<option data-id="' + item.areaId
                        + '">' + item.areaName + '</option>';
                });
                $('#shop-category').html(tmpShopCategoryHtml);
                $('#area').html(tmpAreaHtml);
            }
        });
    }

    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tmpAreaHtml = '';
                data.areaList.map(function (value, index) {
                    tmpAreaHtml += '<option data-id="' + value.areaId + '">'
                        + value.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory);
                // 不允许对店铺列表进行修改
                $('#shop-category').attr('disabled', 'disabled');
                $('#area').html(tmpAreaHtml);
                $("#area option[data-id='" + shop.area.areaId + "]").attr("selected", "selected");
            }
        })
    }

    $('#submit').click(function () {
        var shop = {};
        if (isModify) {
            shop.shopId = shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.shopCategory = {
            shopCategoryId : $('#shop-category').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId : $('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        var shopImg = $('#shop-img')[0].files[0];
        var formData = new FormData();
        formData.append('shopImg', shopImg);
        formData.append('shopStr', JSON.stringify(shop));
        var verifyCodeActual = $('#j_kaptcha').val();
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        formData.append('verifyCodeActual', verifyCodeActual);
        $.ajax({
            url : (isModify ? modifyShopUrl : registerShopUrl),
            type : 'POST',
            data : formData,
            contentType : false,
            processData : false,
            cache  :false,
            succss : function (data) {
                if (data.success) {
                    $.toast('提交成功！');
                } else {
                    $.toast('提交失败！'  + data.errMsg);
                }
                $('#kaptcha_img').click();
            }
        });
    });
})