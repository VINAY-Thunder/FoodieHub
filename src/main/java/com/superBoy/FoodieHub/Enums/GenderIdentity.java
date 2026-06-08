package com.superBoy.FoodieHub.Enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GenderIdentity {
	MALE,
	FEMALE,
	@JsonProperty("OTHER")
	OTHERS
}
