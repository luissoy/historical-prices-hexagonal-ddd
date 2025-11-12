package com.luissoy.historicalprices.infrastructure.out.persistence.product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductDataAccessTest {

    private ProductDataAccess dataAccess;
    private JdbcTemplate jdbc;
    private ProductEntityMapper mapper;

    @BeforeEach
    void setUp() {
        jdbc = mock(JdbcTemplate.class);
        mapper = mock(ProductEntityMapper.class);
        dataAccess = new ProductDataAccess(
                jdbc,
                mapper,
                new SimpleJdbcInsert(jdbc)
                        .withTableName("PRODUCTS")
                        .usingGeneratedKeyColumns("id")
        );
    }

    @Test
    void findById_shouldReturnMappedProduct() {
        ProductId id = new ProductId(1L);
        Product product = mock(Product.class);

        when(jdbc.query(anyString(), any(RowMapper.class), anyLong()))
                .thenReturn(java.util.List.of(product));

        Optional<Product> result = dataAccess.findById(id);

        assertThat(result).isPresent();
    }

    @Test
    void findAll_shouldReturnList() {
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(java.util.List.of(product1, product2));

        assertThat(dataAccess.findAll()).hasSize(2);
    }


    @Test
    void saveBasicInfo_shouldInsertOrUpdateProduct() {
        Product product = new Product(
                null,
                new ProductName("New Product"),
                new ProductDescription("New Desc")
        );

        ProductEntity entity = new ProductEntity(null, "New Product", "New Desc");
        when(mapper.toEntity(product)).thenReturn(entity);

        SimpleJdbcInsert insertMock = mock(SimpleJdbcInsert.class);
        when(insertMock.executeAndReturnKey(anyMap())).thenReturn(1L);

        ProductDataAccess dataAccessWithMockInsert = new ProductDataAccess(jdbc, mapper, insertMock);

        dataAccessWithMockInsert.saveBasicInfo(product);

        verify(insertMock, atLeastOnce()).executeAndReturnKey(anyMap());
        verify(jdbc, never()).update(anyString(), any(), any());
    }

    @Test
    void delete_shouldCallJdbcUpdate() {
        ProductId id = new ProductId(1L);
        dataAccess.delete(id);
        verify(jdbc).update(anyString(), eq(id.getValue()));
    }
}
