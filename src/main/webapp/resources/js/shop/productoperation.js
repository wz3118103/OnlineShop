$(function () {
    var productId = getQueryString('productId');
    var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
    var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
    var modifyOrAddProductUrl = '/o2o/shopadmin/modifyproduct';

    var isModify = false;
    if (productId) {
        getInfo(productId);
        isModify = true;
    } else {
        getCategory();
        modifyOrAddProductUrl = '/o2o/shopadmin/addproduct';
    }

    /**
     * 获取需要编辑的商品信息，并赋值给表单
     * @param id
     */
    function getInfo(id) {
        $.getJSON(infoUrl, function (data) {
            if (data.success) {
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);

                var optionHtml = '';
                var optionArray = data.productCategoryList;
                // 默认选择编辑前的商品类别
                var optionSelected = product.productCategory.productCategoryId;
                optionArray.map(function (item, index) {
                    var isSelect = (optionSelected == item.productCategoryId ? 'selected' : '');
                    optionHtml += '<option data-value="' + item.productCategoryId + '"' + isSelect + '> '
                                        + item.productCategoryName
                                + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    /**
     * 为商品添加操作提供该商店下的所有商品列表
     */
    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if(data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList.map(function (item, index) {
                    optionHtml += '<option data-value="' + item.productCategoryId + '"> '
                                    + item.productCategoryName
                                + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }

    /**
     * 商品详情图
     * 控件最后一个元素上传了图片时，且总数未达到6个，则生成一个新的上传控件
     */
    $('.detail-img-div').on('change', '.detail-img:last-child', function () {
       if ($('.detail-img').length < 6) {
           $('#detail-img').append('<input type="file" class="detail-img">');
       }
    });

    $('#submit').click(
        function () {
            var product = {};
            product.productName = $('#product-name').val();
            product.productDesc = $('#product-desc').val();
            product.priority = $('#priority').val();
            product.normalPrice = $('#normal-price').val();
            product.promotionPrice = $('#promotion-price').val();
            product.productCategory = {
                productCategoryId : $('#category').find('option').not(
                    function () {
                        return !this.selected;
                    }).data('value')
            };
            product.productId = productId;

            var thumbnail = $('#small-img')[0].files[0];

            var formData = new FormData();
            formData.append('thumbnail', thumbnail);

            $('.detail-img').map (function (index, item) {
                if ($('.detail-img')[index].files.length > 0) {
                    formData.append('productImg' + index, $('.detail-img')[index].files[0]);
                }
            });

            formData.append('productStr', JSON.stringify(product));

            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            $.ajax({
               url : modifyOrAddProductUrl,
               type : 'POST',
               data : formData,
               contentType : false,
               processData : false,
               cache : false,
               success : function (data) {
                   if (data.success) {
                       $.toast('提交成功！');
                   } else {
                       $.toast('提交失败！');
                   }
                   $('#captcha_img').click();
               }
            });
        }
    );
});