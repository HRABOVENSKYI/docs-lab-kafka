package ua.lviv.iot.docslab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lviv.iot.docslab.dto.OrderCsvDto;
import ua.lviv.iot.docslab.dto.OrderDto;
import ua.lviv.iot.docslab.mapper.OrderMapper;
import ua.lviv.iot.docslab.model.Order;
import ua.lviv.iot.docslab.repositiry.OrderDao;
import ua.lviv.iot.docslab.repositiry.csv.CsvUtils;
import ua.lviv.iot.docslab.service.strategy.WriteStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;

    private static final Random random = new Random();

    public List<Order> findAll() {
        return orderDao.findAll();
    }

    public Order findById(Long id) {
        return orderDao.findById(id)
                .orElseThrow(() -> new RuntimeException("InsuranceResponse not found"));
    }

    public List<OrderCsvDto> generateOrdersCsv() {
        List<OrderCsvDto> orders = generateOrderCsvDtos(1000);
        CsvUtils.writeOrdersToCsv(orders);
        return orders;
    }

    private List<OrderCsvDto> generateOrderCsvDtos(int num) {
        List<OrderCsvDto> orders = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            orders.add(new OrderCsvDto(
                    random.nextLong(5) + 1,
                    random.nextLong(5) + 1,
                    LocalDateTime.now()));
        }
        return orders;
    }

    public List<Order> saveCsvToDb() {
        final List<Order> orders = CsvUtils.readOrdersFromCsv()
                .stream()
                .map(orderMapper::map)
                .toList();
        return orderDao.saveAll(orders);
    }
}
