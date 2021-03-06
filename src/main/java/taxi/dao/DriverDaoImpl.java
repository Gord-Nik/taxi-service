package taxi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import taxi.lib.Dao;
import taxi.lib.exception.DataProcessingException;
import taxi.model.Driver;
import taxi.util.ConnectionUtil;

@Dao
public class DriverDaoImpl implements DriverDao {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Driver create(Driver driver) {
        String query = "INSERT INTO drivers (name, license_number, login, password) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenseNumber());
            statement.setString(3, driver.getLogin());
            statement.setString(4, driver.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                driver.setId(resultSet.getObject(1, Long.class));
            }
            logger.info("Driver {} added to DB", driver);
            return driver;
        } catch (SQLException e) {
            logger.error(String.format("Can`t add driver %s to DB", driver), e.getMessage());
            throw new DataProcessingException("Couldn't create "
                    + driver + ". ", e);
        }
    }

    @Override
    public Optional<Driver> get(Long id) {
        String query = "SELECT * FROM drivers WHERE id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Driver driver = null;
            if (resultSet.next()) {
                driver = getDriver(resultSet);
            }
            logger.info("Got driver from DB by id = {}", id);
            return Optional.ofNullable(driver);
        } catch (SQLException e) {
            logger.error(String.format("Couldn't get driver from DB with id = %s", id),
                    e.getMessage());
            throw new DataProcessingException("Couldn't get driver by id " + id, e);
        }
    }

    @Override
    public List<Driver> getAll() {
        String query = "SELECT * FROM drivers WHERE deleted = FALSE";
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                drivers.add(getDriver(resultSet));
            }
            logger.info("Got all drivers from DB");
            return drivers;
        } catch (SQLException e) {
            logger.error("Couldn't get all drivers from DB: {}", e.getMessage());
            throw new DataProcessingException("Couldn't get a list of drivers from driversDB.",
                    e);
        }
    }

    @Override
    public Driver update(Driver driver) {
        String query = "UPDATE drivers "
                + "SET name = ?, license_number = ? "
                + "WHERE id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement
                        = connection.prepareStatement(query)) {
            statement.setString(1, driver.getName());
            statement.setString(2, driver.getLicenseNumber());
            statement.setLong(3, driver.getId());
            statement.executeUpdate();
            logger.info("Updated {} in DB", driver);
            return driver;
        } catch (SQLException e) {
            logger.error(String.format("Couldn't update %s in DB", driver)
                    + "{}",e.getMessage());
            throw new DataProcessingException("Couldn't update "
                    + driver + " in driversDB.", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE drivers SET deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            logger.info("Deleted driver from DB by id = {}", id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(String.format("Couldn't delete driver from DB with id = %s", id),
                    e.getMessage());
            throw new DataProcessingException("Couldn't delete driver with id " + id, e);
        }
    }

    private Driver getDriver(ResultSet resultSet) throws SQLException {
        Long newId = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        String licenseNumber = resultSet.getString("license_number");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        Driver driver = new Driver(name, licenseNumber);
        driver.setId(newId);
        driver.setLogin(login);
        driver.setPassword(password);
        return driver;
    }

    @Override
    public Optional<Driver> findByLogin(String login) {
        String query = "SELECT * FROM drivers WHERE login = ? AND deleted = FALSE;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            Driver driver = null;
            if (resultSet.next()) {
                driver = getDriver(resultSet);
            }
            logger.info("Got driver by login: {}", login);
            return Optional.ofNullable(driver);
        } catch (SQLException e) {
            logger.error(String.format("Couldn't get driver by login: %s", login) + "{}",
                    e.getMessage());
            throw new DataProcessingException("Couldn't get driver by login " + login, e);
        }
    }
}
