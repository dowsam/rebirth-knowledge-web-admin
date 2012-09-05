function eXcell_stree(cell) {
	if (cell) {
		this.cell = cell;
		this.grid = this.cell.parentNode.grid;
		if (!this.grid._sub_trees)
			return;
		this._sub = this.grid._sub_trees[cell._cellIndex];
		if (!this._sub)
			return;
		this._sub = this._sub[0]
	}
	;
	this.getValue = function() {
		return this.cell._val
	};
	this.setValue = function(val) {
		this.cell._val = val;
		if (val != null && val != undefined && val != "") {
			val = val + "";
			var ids = val.split(",");
			var texts = new Array();
			for ( var int = 0; int < ids.length; int++) {
				texts.push(this._sub.getItemText(ids[int]));
			}
			val = texts.join(",");
		} else {
			val = this._sub.getItemText(this.cell._val);
		}
		this.setCValue((val || "&nbsp;"), val)
	};
	this.edit = function() {
		this._sub.parentObject.style.display = 'block';
		var arPos = this.grid.getPosition(this.cell);
		this._sub.parentObject.style.top = arPos[1] + "px";
		this._sub.parentObject.style.left = arPos[0] + "px";
		this._sub.parentObject.style.position = "absolute";
		var a = this.grid.editStop;
		this.grid.editStop = function() {
		};
		if (this.cell._val) {
			var allValues = this._sub.getAllChecked();
			if (allValues != null && allValues != undefined && allValues != "") {
				allValues = allValues + "";
				allValues = allValues.split(",");
				for ( var int = 0; int < allValues.length; int++) {
					this._sub.setCheck(allValues[int], false);
				}
			}
			var v = this.cell._val + "";
			var ids = v.split(",");
			if (ids && ids != "") {
				for ( var int = 0; int < ids.length; int++) {
					this._sub.setCheck(ids[int], true);
				}
			}
		}
		this.grid.editStop = a
	};
	this.detach = function() {
		this._sub.parentObject.style.display = 'none';
		if (this.grid._sub_id != null) {
			var old = this.cell._val;
			var allValues = this._sub.getAllChecked();
			if (allValues != null && allValues != undefined && allValues != "") {
				this.setValue(allValues);
				allValues = allValues + "";
				allValues = allValues.split(",");
				for ( var int = 0; int < allValues.length; int++) {
					this._sub.setCheck(allValues[int], false);
				}
			} else {
				this.setValue(this._sub.getSelectedItemId());
			}
			this.grid._sub_id = null;
			return this.cell._val != old
		}
	}
};
eXcell_stree.prototype = new eXcell;
dhtmlXGridObject.prototype.setSubTree = function(tree, s_index) {
	if (!this._sub_trees)
		this._sub_trees = [];
	this._sub_trees[s_index] = [ tree ];
	tree.parentObject.style.display = "none";
	var that = this;
	tree.parentObject.onclick = function(event) {
		(event || window.event).cancelBubble = true;
		return false
	};
	tree.ev_onDblClick = null;
	tree.attachEvent("onDblClick", function(id) {
		tree.setCheck(id, true);
		that._sub_id = id;
		that.editStop();
		return true
	});
	tree._chRRS = true
};