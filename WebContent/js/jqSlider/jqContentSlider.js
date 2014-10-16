/**
 * jqContentSlider
 * A generic jQueryUI widget to support sliding elements with mouse and touch
 * 
 * Based on jQRangeSlider by Guillaume Gautreau
 */

/**
 *
 * Copyright (C) Guillaume Gautreau 2012
 * Dual licensed under the MIT or GPL Version 2 licenses.
 *
 */

(function ($, undefined) {
	"use strict";

	$.widget("ui.contentSlider", $.ui.draggable, {
		options: {
			bounds: {min:0, max:100},
			defaultValues: {min:20, max:50},
			wheelMode: null,
			wheelSpeed: 4,
			arrows: true,
			valueLabels: "show",
			formatter: null,
			durationIn: 0,
			durationOut: 400,
			delayOut: 200,
			range: {min: false, max: false},
			step: false,
			scales: false,
			enabled: true,
			symmetricPositionning: false
		},

		_values: null,
		_valuesChanged: false,
		_initialized: false,

		// Created elements
		container: null,
		content: null,
		changing: {min:false, max:false},
		changed: {min:false, max:false},
		
		_create: function(){
			$.ui.draggable.prototype._create.apply(this);
			
			this._setDefaultValues();

			this.labels = {left: null, right:null, leftDisplayed:true, rightDisplayed:true};
			this.arrows = {left:null, right:null};
			this.changing = {min:false, max:false};
			this.changed = {min:false, max:false};

			this._createElements();
		},

		_setDefaultValues: function(){
			this._values = {
				min: this.options.defaultValues.min,
				max: this.options.defaultValues.max
			};
		},
		
		_createElements: function(){
			if (this.element.css("position") !== "absolute"){
				this.element.css("position", "relative");
			}

			this.element.addClass("ui-contentSlider")
				.css("overflow", "hidden")
				.css("cursor", "grabbing");
			
			var content = this.element.html();
			
			this.element.empty();

			this.container = $("<div/>")
			.addClass("ui-contentSlider-container")
			.appendTo(this.element);

			this.container.html(content);
			
			if (!this.options.enabled) this._toggle(this.options.enabled);
		}
	});
}(jQuery));
