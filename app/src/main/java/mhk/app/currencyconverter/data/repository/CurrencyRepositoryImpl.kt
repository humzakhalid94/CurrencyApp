package mhk.app.currencyconverter.data.repository

import com.example.anime.data.data_source.dto.CurrencyInstanceAPI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mhk.app.currencyconverter.data.data_source.dto.CurrencyDTO
import mhk.app.currencyconverter.data.data_source.dto.CurrencyGenericDTO
import mhk.app.currencyconverter.data.data_source.dto.Rates
import mhk.app.currencyconverter.domain.common.base.BaseResult
import mhk.app.currencyconverter.domain.model.CurrencyEntity
import mhk.app.currencyconverter.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(private val productApi: CurrencyInstanceAPI) :
    CurrencyRepository {


    override suspend fun getCurrencyList(): Flow<BaseResult<List<CurrencyEntity>, CurrencyDTO>> {
        return flow {
            val response = productApi.getCurrencies()

            if (response.isSuccessful){
                val body = response.body()!!
                val data = body.symbols

                val currency = mutableListOf<CurrencyEntity>()

                var currencyEntity: CurrencyEntity?
                data.forEach { (key, value) ->
//                    "AED": "United Arab Emirates Dirham",
                    currencyEntity = CurrencyEntity(
                        symbol = key,
                        name = value
                    )
                    currency.add(currencyEntity!!)
                }
                emit(BaseResult.Success(currency))
            }else{
                val type = object : TypeToken<CurrencyDTO>(){}.type
                val err = Gson().fromJson<CurrencyDTO>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

//    override suspend fun getProductById(id: String): Flow<BaseResult<CurrencyEntity, WrappedResponse<ProductResponse>>> {
//        return flow {
//            val response = productApi.getProductById(id)
//            if(response.isSuccessful){
//                val body = response.body()!!
//                val user = ProductUserEntity(body.data?.user?.id!!, body.data?.user?.name!!, body.data?.user?.email!!)
//                val product = CurrencyEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
//                emit(BaseResult.Success(product))
//            }else{
//                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
//                val err = Gson().fromJson<WrappedResponse<ProductResponse>>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
//                emit(BaseResult.Error(err))
//            }
//        }
//    }
//
//    override suspend fun updateProduct(productUpdateRequest: ProductUpdateRequest, id: String): Flow<BaseResult<CurrencyEntity, WrappedResponse<ProductResponse>>> {
//        return flow {
//            val response = productApi.updateProduct(productUpdateRequest, id)
//            if(response.isSuccessful){
//                val body = response.body()!!
//                val user = ProductUserEntity(body.data?.user?.id!!, body.data?.user?.name!!, body.data?.user?.email!!)
//                val product = CurrencyEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
//                emit(BaseResult.Success(product))
//            }else{
//                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
//                val err = Gson().fromJson<WrappedResponse<ProductResponse>>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
//                emit(BaseResult.Error(err))
//            }
//        }
//    }
//
//    override suspend fun deleteProductById(id: String): Flow<BaseResult<Unit, WrappedResponse<ProductResponse>>> {
//        return flow{
//            val response = productApi.deleteProduct(id)
//            if(response.isSuccessful){
//                emit(BaseResult.Success(Unit))
//            }else{
//                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
//                val err = Gson().fromJson<WrappedResponse<ProductResponse>>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
//                emit(BaseResult.Error(err))
//            }
//        }
//    }
//
//    override suspend fun createProduct(productCreateRequest: ProductCreateRequest): Flow<BaseResult<CurrencyEntity, WrappedResponse<ProductResponse>>> {
//        return flow {
//            val response = productApi.createProduct(productCreateRequest)
//            if(response.isSuccessful){
//                val body = response.body()!!
//                val user = ProductUserEntity(body.data?.user?.id!!, body.data?.user?.name!!, body.data?.user?.email!!)
//                val product = CurrencyEntity(body.data?.id!!, body.data?.name!!, body.data?.price!!, user)
//                emit(BaseResult.Success(product))
//            }else{
//                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
//                val err = Gson().fromJson<WrappedResponse<ProductResponse>>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
//                emit(BaseResult.Error(err))
//            }
//        }
//    }


}