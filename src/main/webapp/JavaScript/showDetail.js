/**
 * 
 */
function showDetail(){
	const checkdetail = document.getElementById("checkdetail");//チェックボックスの値
	const detailcolumn = document.querySelectorAll("#detail");//id="detail"であるすべての列
	//チェックボックスがチェックされていればid="detail"であるすべての列を表示状態に、そうでなければ非表示に
	if(checkdetail.checked == true){//TypeError: Cannot read properties of null (reading 'checked')
		detailcolumn.addClass("d-block"); 
		detailcolumn.removeClass("d-none");
	} else{
		detailcolumn.addClass("d-none");
		detailcolumn.removeClass("d-block"); 
	}
}