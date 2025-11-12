package com.luissoy.historicalprices.infrastructure.out.persistence.product;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductRepositoryAdapterTest {

    private ProductRepositoryAdapter repository;
    private ProductDataAccess dataAccess;
    private ProductAggregateAssembler assembler;

    @BeforeEach
    void setUp() {
        dataAccess = mock(ProductDataAccess.class);
        assembler = mock(ProductAggregateAssembler.class);
        repository = new ProductRepositoryAdapter(dataAccess, assembler);
    }

    @Test
    void findById_shouldDelegateToDataAccessAndAssembler() {
        Product product = mock(Product.class);
        when(dataAccess.findById(any())).thenReturn(Optional.of(product));
        when(assembler.loadPrices(product)).thenReturn(product);

        Optional<Product> result = repository.findById(new ProductId(1L));
        assertThat(result).contains(product);
    }

    @Test
    void findAll_shouldMapThroughAssembler() {
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        when(dataAccess.findAll()).thenReturn(List.of(product1, product2));
        when(assembler.loadPrices(any())).thenAnswer(inv -> inv.getArgument(0));

        List<Product> result = repository.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void save_shouldCallDataAccessAndSyncPrices() {
        Product product = mock(Product.class);
        Product saved = mock(Product.class);
        ProductId savedId = new ProductId(1L);

        when(saved.id()).thenReturn(savedId);
        when(product.id()).thenReturn(null);

        when(dataAccess.saveBasicInfo(product)).thenReturn(saved);

        doNothing().when(assembler).syncPrices(any());

        ProductRepositoryAdapter repoSpy = spy(new ProductRepositoryAdapter(dataAccess, assembler));
        doReturn(Optional.of(saved)).when(repoSpy).findById(savedId);

        Product result = repoSpy.save(product);

        assertThat(result).isEqualTo(saved);
        verify(dataAccess).saveBasicInfo(product);
        verify(assembler).syncPrices(any());
        verify(repoSpy).findById(savedId);
    }


    @Test
    void delete_shouldDelegateToDataAccess() {
        ProductId id = new ProductId(1L);
        repository.delete(id);
        verify(dataAccess).delete(id);
    }
}
