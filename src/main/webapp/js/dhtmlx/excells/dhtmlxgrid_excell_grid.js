var arr;
var temp_arr;
function eXcell_grid(cell) {
	if (cell) {
		this.cell = cell;
		this.grid = this.cell.parentNode.grid;
		if (!this.grid._sub_grids)
			return;
		this._sub = this.grid._sub_grids[cell._cellIndex];
		if (!this._sub)
			return;
		this._sindex = this._sub[1];
		this._sub = this._sub[0]
	}
	;
	this.getValue = function() {
		return this.cell.val
	};
	this.setValue = function(val) {
		this.cell.val = val;
		var value = val;
		var as=val.split(",");
		var vs=new Array();
		for ( var int = 0; int < as.length; int++) {
			if (this._sub.getRowById(as[int])) {
				val = this._sub.cells(as[int], this._sindex);
				if (val)
					val = val.getValue();
				else
					val = "";
				vs.push(val);
			}
		}
		if(vs.length>0){
			val=vs.join(",");
		}
		this.setCValue((val || "&nbsp;"), val)

		/**
		 * xiaoli 2009-07-01 ������������
		 */
		if (this._sub.getRowById(value)) {
			if (arr != undefined) {
				var tem = arr.split("|");
				for ( var i = 0; i < tem.length; i++) {
					var temp = tem[i].split(":");
					var v = this._sub.cells(value, temp[1]);
					if (v) {
						v = v.getValue();
					} else {
						v = ""
					}

					this.grid.cells(this.cell.parentNode.idd, temp[0])
							.setValue(v);
				}
			}
			if (temp_arr != undefined) {
				var tem = temp_arr.split("|");
				for ( var i = 0; i < tem.length; i++) {
					var temp = tem[i].split(":");
					var v = this._sub.cells(value, temp[1]);
					if (v) {
						v = v.getValue();
					} else {
						v = ""
					}

					this.grid.cells(this.cell.parentNode.idd, temp[0])
							.setValue(v);
				}
			}
		}
		/**
		 * ����
		 */
	};
	this.edit = function() {
		this.val = this.cell.val;
		this._sub.entBox.style.display = 'block';
		var arPos = this.grid.getPosition(this.cell);
		this._sub.entBox.style.top = arPos[1] + "px";
		this._sub.entBox.style.left = arPos[0] + "px";
		this._sub.entBox.style.position = "absolute";
		this._sub.setSizes();
		var a = this.grid.editStop;
		this.grid.editStop = function() {
		};
		if(this.cell.val!=null && this.cell.val!=undefined){
			var vs=(this.cell.val)+"".split(",");
			for ( var int = 0; int < vs.length; int++) {
				if (this._sub.getRowById(vs[int])){
					this._sub.setSelectedRow(vs[int]);
					this._sub.cells(vs[int],0).setValue(1);
				}
			}
		}
		this._sub.setActive(true);
		this.grid.editStop = a
	};
	this.detach = function() {
		var old = this.cell.val;
		this._sub.entBox.style.display = 'none';
		if (this._sub.getSelectedId() === null)
			return false;
		var ids=this._sub.getCheckedRows(0);
		var _ids=ids.split(",");
		this.setValue(ids);
		for ( var int = 0; int < _ids.length; int++) {
			if(_ids[int]!=null && _ids[int]!=undefined && _ids[int]!=""){
				this._sub.cells(_ids[int],0).setValue(0);
			}
		}
		this.grid.setActive(true);
		return this.cell.val != old
	}
};
eXcell_grid.prototype = new eXcell;
dhtmlXGridObject.prototype.setSubGrid = function(grid, s_index, t_index) {
	if (!this._sub_grids)
		this._sub_grids = [];
	this._sub_grids[s_index] = [ grid, t_index ];
	grid.entBox.style.display = "none";
	var that = this;
	grid.attachEvent("onRowSelect", function(id) {
		grid.cells(id,0).setValue(1);
		that.editStop();
		return true
	});
	grid._chRRS = false
};
/*******************************************************************************
 * 2010-07-02 xiaoli
 * 
 * @param {}
 *            a
 */
dhtmlXGridObject.prototype.setArr = function(a) {
	arr = a;
}
dhtmlXGridObject.prototype.setTempArr = function(temparr) {
	temp_arr = temparr;
}
/*******************************************************************************
 * end
 */
