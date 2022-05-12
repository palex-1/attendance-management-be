package it.palex.attendanceManagement.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.HttpCodes;


/**
 * @author Alessandro Pagliaro
 *
 */
public interface GenericService extends BasicGenericService {	

	default <T> GenericResponse<Page<T>> buildPageableOkResponse(List<T> list,
			int totalCount, Pageable pageable){
		GenericResponse<Page<T>> res = new GenericResponse<>();
		res.setData(new PageImpl<T>(list, pageable, totalCount));
		res.setCode(HttpCodes.OK);
		
		return res;
	}
	
	default <T> GenericResponse<Page<T>> buildPageableOkResponse(List<T> list,
			long totalCount, Pageable pageable){
		GenericResponse<Page<T>> res = new GenericResponse<>();
		res.setData(new PageImpl<T>(list, pageable, totalCount));
		res.setCode(HttpCodes.OK);
		
		return res;
	}
	
	default <T> GenericResponse<Page<T>> buildPageableOkResponse(List<T> list,
			long totalCount, Pageable pageable, StandardReturnCodesEnum error){
		GenericResponse<Page<T>> res = new GenericResponse<>();
		res.setData(new PageImpl<T>(list, pageable, totalCount));
		res.setCode(HttpCodes.OK);
		res.setMessage(error.getMess());
		res.setSubcode(error.getCode());
		
		return res;
	}
	
	
	default GenericResponse<StringDTO> buildStringOkResponse(String data){
		return new GenericResponse<StringDTO>(new StringDTO(data), HttpCodes.OK, "Success");
	}
	
	default GenericResponse<StringDTO> buildStringOkResponse(String data, StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildStringOkResponse(data);
		}
		return this.buildStringOkResponse(data, error.getMess(), error.getCode());
	}
	
	default GenericResponse<StringDTO> buildStringOkResponse(String data, int subcode){
		return new GenericResponse<StringDTO>(new StringDTO(data), HttpCodes.OK, "Success", subcode);
	}
	
	default GenericResponse<StringDTO> buildStringOkResponse(String data, String msg){
		return new GenericResponse<StringDTO>(new StringDTO(data), HttpCodes.OK, msg);
	}
	
	default GenericResponse<StringDTO> buildStringOkResponse(String data, String msg, int subcode){
		return new GenericResponse<StringDTO>(new StringDTO(data), HttpCodes.OK, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildOkResponse(T data, int subcode){
		return new GenericResponse<T>(data, HttpCodes.OK, "Success", subcode);
	}
	
	default <T> GenericResponse<T> buildOkResponse(T data, StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildOkResponse(data);
		}
		return this.buildOkResponse(data, error.getMess(), error.getCode());
	}
	
	default <T> GenericResponse<T> buildOkResponse(T data){
		return new GenericResponse<T>(data, HttpCodes.OK, "Success");
	}
	
	default <T> GenericResponse<T> buildOkResponse(T data, String msg, int subcode){
		return new GenericResponse<T>(data, HttpCodes.OK, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildOkResponse(T data, String msg){
		return new GenericResponse<T>(data, HttpCodes.OK, msg);
	}
	
	default <T> GenericResponse<T> buildBadDataResponse(){
		return new GenericResponse<T>(null, HttpCodes.BAD_REQUEST, "Bad Data");
	}
	
	default <T> GenericResponse<T> buildBadDataResponse(String msg){
		return new GenericResponse<T>(null, HttpCodes.BAD_REQUEST, msg);
	}
	
	default <T> GenericResponse<T> buildBadDataResponse(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.BAD_REQUEST, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildBadDataResponse(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildBadDataResponse();
		}
		return this.buildBadDataResponse(error.getMess(), error.getCode());
	}
	
		default <T> GenericResponse<T> buildEnhanceYourCalmResponse(){
		return new GenericResponse<T>(null, HttpCodes.ENHANCE_YOUR_CALM, "Enhance your calm"); 
	}
	
	default <T> GenericResponse<T> buildEnhanceYourCalmResponse(String msg){
		return new GenericResponse<T>(null, HttpCodes.ENHANCE_YOUR_CALM, msg); 
	}
	
	default <T> GenericResponse<T> buildEnhanceYourCalmResponse(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.ENHANCE_YOUR_CALM, msg, subcode); 
	}
	
	default <T> GenericResponse<T> buildEnhanceYourCalmResponse(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildEnhanceYourCalmResponse();
		}
		return this.buildEnhanceYourCalmResponse(error.getMess(), error.getCode());
	}
	
	default <T> GenericResponse<T> buildForbiddenResponse(){
		return new GenericResponse<T>(null, HttpCodes.FORBIDDEN, "Forbidden"); 
	}
	
	default <T> GenericResponse<T> buildForbiddenResponse(String msg){
		return new GenericResponse<T>(null, HttpCodes.FORBIDDEN, msg); 
	}
	
	default <T> GenericResponse<T> buildForbiddenResponse(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.FORBIDDEN, msg, subcode); 
	}
	
	default <T> GenericResponse<T> buildForbiddenResponse(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildForbiddenResponse();
		}
		return this.buildForbiddenResponse(error.getMess(), error.getCode());
	}
	
	
	default <T> GenericResponse<T> buildUnauthorizedResponse(){
		return new GenericResponse<T>(null, HttpCodes.UNAUTHORIZED, "Unauthorized");
	}
	
	default <T> GenericResponse<T> buildUnauthorizedResponse(String msg){
		return new GenericResponse<T>(null, HttpCodes.UNAUTHORIZED, msg);
	}
	
	default <T> GenericResponse<T> buildUnauthorizedResponse(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.UNAUTHORIZED, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildUnauthorizedResponse(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildUnauthorizedResponse();
		}
		return this.buildUnauthorizedResponse(error.getMess(), error.getCode());
	}
	
	
	
	default <T> GenericResponse<T> buildNotFoundResponse(){
		return new GenericResponse<T>(null, HttpCodes.NOT_FOUND, "Not found");
	}
	
	default <T> GenericResponse<T> buildNotFoundResponse(String msg){
		return new GenericResponse<T>(null, HttpCodes.NOT_FOUND, msg);
	}
	
	default <T> GenericResponse<T> buildNotFoundResponse(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.NOT_FOUND, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildNotFoundResponse(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildNotFoundResponse();
		}
		return this.buildNotFoundResponse(error.getMess(), error.getCode());
	}
	
	
	default <T> GenericResponse<T> buildNotAcceptableResponse(){
		return new GenericResponse<T>(null, HttpCodes.NOT_ACCEPTABLE, "Not Acceptable");
	}	
	
	default <T> GenericResponse<T> buildNotAcceptableResponse(String msg){
		return new GenericResponse<T>(null, HttpCodes.NOT_ACCEPTABLE, msg);
	}
	
	default <T> GenericResponse<T> buildNotAcceptableResponse(String msg, int subcode){
		GenericResponse<T> res =  new GenericResponse<T>(null, HttpCodes.NOT_ACCEPTABLE, msg, subcode);
		return res;
	}
	
	default <T> GenericResponse<T> buildNotAcceptableResponse(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildNotAcceptableResponse();
		}
		return this.buildNotAcceptableResponse(error.getMess(), error.getCode());
	}
	
	
	default <T> GenericResponse<T> buildTooManyRequest(){
		return new GenericResponse<T>(null, HttpCodes.TOO_MANY_REQUESTS, "Too Many Requests");
	}
	
	default <T> GenericResponse<T> buildTooManyRequest(String msg){
		return new GenericResponse<T>(null, HttpCodes.TOO_MANY_REQUESTS, msg);
	}
	
	default <T> GenericResponse<T> buildTooManyRequest(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.TOO_MANY_REQUESTS, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildTooManyRequest(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildTooManyRequest();
		}
		return this.buildTooManyRequest(error.getMess(), error.getCode());
	}
	
	
	default <T> GenericResponse<T> buildUnprocessableEntity(){
		return new GenericResponse<T>(null, HttpCodes.UNPROCESSABLE_ENTITY, "Unprocessable Entity");
	}
	
	default <T> GenericResponse<T> buildUnprocessableEntity(String msg){
		return new GenericResponse<T>(null, HttpCodes.UNPROCESSABLE_ENTITY, msg);
	}
	
	default <T> GenericResponse<T> buildUnprocessableEntity(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.UNPROCESSABLE_ENTITY, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildUnprocessableEntity(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildUnprocessableEntity();
		}
		return this.buildUnprocessableEntity(error.getMess(), error.getCode());
	}
	
	
	
	default <T> GenericResponse<T> buildConflictEntity(){
		return new GenericResponse<T>(null, HttpCodes.CONFLICT, "Entities are conflicting");
	}
	
	default <T> GenericResponse<T> buildConflictEntity(String msg){
		return new GenericResponse<T>(null, HttpCodes.CONFLICT, msg);
	}
	
	default <T> GenericResponse<T> buildConflictEntity(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildConflictEntity();
		}
		return this.buildConflictEntity( error.getMess(), error.getCode());
	}
	
	default <T> GenericResponse<T> buildConflictEntity(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.CONFLICT, msg, subcode);
	}
	
	
	
	default <T> GenericResponse<T> buildInternalServerError(){
		return new GenericResponse<T>(null, HttpCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
	}
	
	default <T> GenericResponse<T> buildInternalServerError(String msg){
		return new GenericResponse<T>(null, HttpCodes.INTERNAL_SERVER_ERROR, msg);
	}
	
	default <T> GenericResponse<T> buildInternalServerError(String msg, int subcode){
		return new GenericResponse<T>(null, HttpCodes.INTERNAL_SERVER_ERROR, msg, subcode);
	}
	
	default <T> GenericResponse<T> buildInternalServerError(StandardReturnCodesEnum error) {
		if(error==null) {
			return this.buildInternalServerError();
		}
		return this.buildInternalServerError(error.getMess(), error.getCode());
	}
}

